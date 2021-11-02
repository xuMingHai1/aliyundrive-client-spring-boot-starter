/*
 * Copyright (c) [2021] [xuMingHai]
 * [aliyundrive-client-spring-boot-starter] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package xyz.xmh.core.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import xyz.xmh.api.FileEnum;
import xyz.xmh.core.ReactiveFileDao;
import xyz.xmh.io.DownloadResource;
import xyz.xmh.pojo.entity.upload.UploadStatus;
import xyz.xmh.pojo.enums.CheckNameEnum;
import xyz.xmh.pojo.request.FileIdRequest;
import xyz.xmh.pojo.request.file.*;
import xyz.xmh.pojo.response.file.CreateFileResponse;
import xyz.xmh.pojo.response.file.DownloadUrlResponse;
import xyz.xmh.util.HashUtils;
import xyz.xmh.util.IoUtils;
import xyz.xmh.util.ProofV1Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author xuMingHai
 */
@Slf4j
public class ReactiveFileDaoImpl implements ReactiveFileDao {

    private final WebClient webClient;

    /**
     * 文件上传重试次数
     */
    private final int uploadRetries;

    /**
     * 指定的分片上传大小
     */
    private final long uploadFragmentation;


    public ReactiveFileDaoImpl(WebClient webClient, int uploadRetries, int uploadFragmentation) {
        this.webClient = webClient;
        this.uploadRetries = uploadRetries;
        this.uploadFragmentation = (long) uploadFragmentation << 20;
    }

    @Override
    public WebClient.ResponseSpec list(ListRequest listRequest) {
        final FileEnum list = FileEnum.LIST;
        return webClient.method(list.getHttpMethod()).uri(list.getApi())
                .bodyValue(listRequest)
                .retrieve();
    }

    @Override
    public WebClient.ResponseSpec search(SearchRequest searchRequest) {
        final FileEnum search = FileEnum.SEARCH;
        return webClient.method(search.getHttpMethod()).uri(search.getApi())
                .bodyValue(searchRequest)
                .retrieve();
    }

    @Override
    public WebClient.ResponseSpec get(String fileId) {
        final FileIdRequest fileIdRequest = new FileIdRequest(fileId);
        final FileEnum get = FileEnum.GET;
        return webClient.method(get.getHttpMethod()).uri(get.getApi())
                .bodyValue(fileIdRequest)
                .retrieve();
    }

    @Override
    public WebClient.ResponseSpec getDownloadUrl(String fileId) {
        final FileIdRequest fileIdRequest = new FileIdRequest(fileId);
        final FileEnum getDownloadUrl = FileEnum.GET_DOWNLOAD_URL;
        return webClient.method(getDownloadUrl.getHttpMethod()).uri(getDownloadUrl.getApi())
                .bodyValue(fileIdRequest)
                .retrieve();
    }

    @Override
    public Mono<ResponseEntity<Resource>> downloadFile(String fileId, HttpHeaders requestHttpHeaders) {
        return getDownloadUrl(fileId).bodyToMono(DownloadUrlResponse.class)
                .mapNotNull(downloadUrlResponse -> {
                    try {
                        return DownloadResource.createResource(downloadUrlResponse, requestHttpHeaders);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).map(downloadResource -> new ResponseEntity<>(downloadResource, downloadResource.getResponseHttpHeaders(), downloadResource.getHttpStatus()));
    }

    @Override
    public WebClient.ResponseSpec getMultiDownloadUrl(MultiDownloadUrlRequest multiDownloadUrlRequest) {
        final FileEnum multiDownloadUrl = FileEnum.MULTI_DOWNLOAD_URL;
        return webClient.method(multiDownloadUrl.getHttpMethod()).uri(multiDownloadUrl.getApi())
                .bodyValue(multiDownloadUrlRequest)
                .retrieve();
    }

    @Override
    public Mono<ResponseEntity<Resource>> multiDownloadFile(MultiDownloadUrlRequest multiDownloadUrlRequest, HttpHeaders httpHeaders) {
        // TODO: 多文件下载不能使用
        return null;
    }

    @Override
    public WebClient.ResponseSpec createFolder(CreateFolderRequest createFolderRequest) {
        final FileEnum createWithFolders = FileEnum.CREATE_WITH_FOLDERS;
        return webClient.method(createWithFolders.getHttpMethod()).uri(createWithFolders.getApi())
                .bodyValue(createFolderRequest)
                .retrieve();
    }

    @Override
    public Mono<CreateFileResponse> uploadFile(String parentFileId, Path path, CheckNameEnum checkNameEnum) {
        // 初始化请求参数
        final CreateFileRequest createFileRequest = new CreateFileRequest();
        createFileRequest.setParentFileId(parentFileId);
        createFileRequest.setCheckNameMode(checkNameEnum);
        createFileRequest.setName(path.getFileName().toString());
        createFileRequest.setSize(path.toFile().length());

        // 文件hash值，判断文件长度是否为0
        if (createFileRequest.getSize() > 0) {
            createFileRequest.setContentHash(HashUtils.sha1(path));
            // 文件大小
            long size = createFileRequest.getSize();
            final List<CreateFileRequest.PartInfo> list = new ArrayList<>();

            // 如果文件大小大于指定的分片大小，则把分片文件
            int i;
            for (i = 1; size > uploadFragmentation; i++) {
                list.add(new CreateFileRequest.PartInfo(i));
                size -= uploadFragmentation;
            }

            if (size != 0) {
                list.add(new CreateFileRequest.PartInfo(i));
            }

            createFileRequest.setPartInfoList(list);

        }

        if (log.isDebugEnabled()) {
            log.debug("【{}】分片数量：{}", createFileRequest.getName(), createFileRequest.getPartInfoList().size());
        }

        // 随机的一段文件字节
        createFileRequest.setProofCode(ProofV1Utils.getCode(path));

        /*
            分片上传需要连续，在使用多线程的情况下，要确保分片上传的连续性，阻塞的线程就会占用大量的内存
         */
        return upload(createFileRequest, 0).map(createFileResponse -> {
            // 判断是否是快传
            if (!createFileResponse.isRapidUpload()) {
                final List<CreateFileResponse.PartInfo> partInfoList = createFileResponse.getPartInfoList();
                try {
                    // 打开一个只读的文件通道
                    final FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);

                    // 上传状态集合
                    final List<UploadStatus> uploadStatusList = new LinkedList<>();

                    // 起始位置
                    long position = 0;
                    for (CreateFileResponse.PartInfo partInfo : partInfoList) {
                        uploadStatusList.add(upload(fileChannel, createFileRequest.getName(), partInfo, position));
                        // 根据指定的分片增加
                        position += uploadFragmentation;
                    }

                    // 如果出现上传失败，根据重试次数重新上传（重试次数小于或等于0不会重新上传）
                    for (int i = 0; i < uploadRetries; i++) {
                        // 如果出现上传成功删除成功的状态
                        uploadStatusList.removeIf(uploadStatus -> uploadStatus.getResponseCode() == 200);
                        // 检查是否存在错误的上传状态
                        if (uploadStatusList.isEmpty()) {
                            break;
                        }
                        log.info("【{}】开始第{}次重新上传........", createFileRequest.getName(), (i + 1));
                        checkUploadStatus(uploadStatusList, fileChannel);
                    }
                    // 关闭文件通道
                    fileChannel.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                final CompleteRequest completeRequest = new CompleteRequest(createFileResponse.getFileId(), createFileResponse.getUploadId());
                final FileEnum complete = FileEnum.COMPLETE;
                webClient.method(complete.getHttpMethod()).uri(complete.getApi())
                        .bodyValue(completeRequest)
                        .retrieve()
                        .toBodilessEntity()
                        .subscribe(voidResponseEntity -> {
                            if (voidResponseEntity.getStatusCode().is2xxSuccessful()) {
                                log.info("【{}】上传成功！！！", createFileRequest.getName());
                            }
                        });

            } else {
                log.info("【{}】秒传成功！！！", createFileRequest.getName());
            }
            return createFileResponse;
        });

    }

    private void checkUploadStatus(List<UploadStatus> uploadStatusList, FileChannel fileChannel) {
        final List<UploadStatus> list = new ArrayList<>(uploadStatusList.size());
        uploadStatusList.forEach(uploadStatus -> {
            log.error("【{}】出现错误的上传分片：{} {}\n错误信息：{}", uploadStatus.getFileName(), uploadStatus.getResponseCode(), uploadStatus.getResponseMessage(), uploadStatus.getErrorMessage());
            list.add(upload(fileChannel, uploadStatus.getFileName(), uploadStatus.getPartInfo(), uploadStatus.getPosition()));
        });
        uploadStatusList.clear();
        uploadStatusList.addAll(list);
    }

    private UploadStatus upload(FileChannel fileChannel, String fileName, CreateFileResponse.PartInfo partInfo, long position) {
        final UploadStatus uploadStatus = new UploadStatus(fileName, partInfo, position);
        try {
            final HttpURLConnection connection = (HttpURLConnection) partInfo.getUploadUrl().openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty(HttpHeaders.CONNECTION, "keep-alive");
            connection.setRequestProperty(HttpHeaders.REFERER, "https://www.aliyundrive.com/");
            connection.setUseCaches(false);
            // 获取连接
            connection.connect();
            if (log.isDebugEnabled()) {
                log.debug("文件【{}】-分片【{}】成功获取连接", fileName, partInfo.getPartNumber());
            }
            // 向连接写入字节
            // 创建一个可写的通道
            final WritableByteChannel writableByteChannel = Channels.newChannel(connection.getOutputStream());
            /*
                这个transferTo方法，在目标通道不是文件类型时，用的fileChannel的map映射
                速度是真滴慢，还占用内存
             */
            fileChannel.transferTo(position, uploadFragmentation, writableByteChannel);
            writableByteChannel.close();
            // 写入状态
            uploadStatus.setResponseCode(connection.getResponseCode());
            uploadStatus.setResponseMessage(connection.getResponseMessage());
            // 如果存在错误流，放入响应状态
            Optional.ofNullable(connection.getErrorStream()).ifPresent(inputStream -> {
                try {
                    uploadStatus.setErrorMessage(IoUtils.toString(inputStream));
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            if (log.isDebugEnabled()) {
                log.debug("文件【{}】-分片【{}】上传完毕", fileName, partInfo.getPartNumber());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadStatus;
    }


    private Mono<CreateFileResponse> upload(CreateFileRequest createFileRequest, int i) {
        // 发送请求
        final FileEnum createWithFolders = FileEnum.CREATE_WITH_FOLDERS;
        final int frequency = i;
        return webClient.method(createWithFolders.getHttpMethod()).uri(createWithFolders.getApi())
                .bodyValue(createFileRequest)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == 409, clientResponse -> {
                    // 阿里云盘的文件上传有时候会出现409，多重复上传几次
                    if (frequency < uploadRetries) {
                        log.error("【{}】上传请求出现409,第【{}】次重新请求", createFileRequest.getName(), (frequency + 1));
                        upload(createFileRequest, frequency + 1);
                    }
                    return clientResponse.createException();
                })
                .bodyToMono(CreateFileResponse.class);

    }

    @Override
    public WebClient.ResponseSpec update(UpdateRequest updateRequest) {
        final FileEnum update = FileEnum.UPDATE;
        return webClient.method(update.getHttpMethod()).uri(update.getApi())
                .bodyValue(updateRequest)
                .retrieve();
    }

}
