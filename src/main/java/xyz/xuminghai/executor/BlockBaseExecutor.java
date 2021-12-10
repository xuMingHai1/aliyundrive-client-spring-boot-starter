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

package xyz.xuminghai.executor;

import org.springframework.http.HttpHeaders;
import xyz.xuminghai.core.ReactiveFileDao;
import xyz.xuminghai.core.ReactiveRecycleDao;
import xyz.xuminghai.pojo.entity.BaseItem;
import xyz.xuminghai.pojo.enums.CheckNameEnum;
import xyz.xuminghai.pojo.request.file.*;
import xyz.xuminghai.pojo.response.file.*;

import java.io.IOException;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;

/**
 * 2021/10/26 14:30 星期二<br/>
 * 阻塞执行器的基本实现
 *
 * @author xuMingHai
 */
public class BlockBaseExecutor extends AbstractExecutor implements BlockExecutor {

    public BlockBaseExecutor(ReactiveFileDao reactiveFileDao, ReactiveRecycleDao reactiveRecycleDao) {
        super(reactiveFileDao, reactiveRecycleDao);
    }

    @Override
    public ListResponse list(ListRequest listRequest) {
        return reactiveFileDao.list(listRequest)
                .bodyToMono(ListResponse.class)
                .block();
    }

    @Override
    public SearchResponse search(SearchRequest searchRequest) {
        return reactiveFileDao.search(searchRequest)
                .bodyToMono(SearchResponse.class)
                .block();
    }

    @Override
    public BaseItem get(String fileId) {
        return reactiveFileDao.get(fileId)
                .bodyToMono(BaseItem.class)
                .block();
    }

    @Override
    public DownloadUrlResponse getDownloadUrl(String fileId) {
        return reactiveFileDao.getDownloadUrl(fileId)
                .bodyToMono(DownloadUrlResponse.class)
                .block();
    }

    @Override
    public long downloadFile(String fileId, Path path, OpenOption... openOptions) {
        final DownloadUrlResponse downloadUrl = getDownloadUrl(fileId);
        try {
            final URLConnection connection = downloadUrl.getUrl().openConnection();
            connection.setRequestProperty(HttpHeaders.REFERER, "https://www.aliyundrive.com/");
            final ReadableByteChannel readableByteChannel = Channels.newChannel(connection.getInputStream());
            final String[] split = connection.getHeaderField(HttpHeaders.CONTENT_DISPOSITION).split("=")[1].split("''");
            final String fileName = URLDecoder.decode(split[1], split[0]);
            final FileChannel fileChannel = FileChannel.open(path.resolve(fileName), openOptions);
            final long l = fileChannel.transferFrom(readableByteChannel, 0, downloadUrl.getSize());
            if (l != downloadUrl.getSize()) {
                throw new IOException("下载异常，实际下载的字节和文件本身的字节不符。期望下载字节：" + downloadUrl.getSize() + "，实际下载字节：" + l);
            }
            readableByteChannel.close();
            fileChannel.close();
            return l;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public CreateFolderResponse createFolder(CreateFolderRequest createFolderRequest) {
        return reactiveFileDao.createFolder(createFolderRequest)
                .bodyToMono(CreateFolderResponse.class)
                .block();
    }

    @Override
    public CreateFileResponse uploadFile(String parentFileId, Path path, CheckNameEnum checkNameEnum) {
        return reactiveFileDao.uploadFile(parentFileId, path, checkNameEnum)
                .block();
    }

    @Override
    public Boolean trash(String fileId) {
        return Boolean.TRUE.equals(reactiveRecycleDao.trash(fileId)
                .toBodilessEntity()
                .map(voidResponseEntity -> voidResponseEntity.getStatusCode().is2xxSuccessful())
                .block());
    }

    @Override
    public BaseItem update(UpdateRequest updateRequest) {
        return reactiveFileDao.update(updateRequest)
                .bodyToMono(BaseItem.class)
                .block();
    }

    @Override
    public VideoPreviewPlayInfoResponse getVideoPreviewPlayInfo(VideoPreviewPlayInfoRequest videoPreviewPlayInfoRequest) {
        return reactiveFileDao.getVideoPreviewPlayInfo(videoPreviewPlayInfoRequest)
                .bodyToMono(VideoPreviewPlayInfoResponse.class)
                .block();
    }

    @Override
    public AudioPlayInfoResponse getAudioPlayInfo(String fileId) {
        return reactiveFileDao.getAudioPlayInfo(fileId)
                .bodyToMono(AudioPlayInfoResponse.class)
                .block();
    }
}
