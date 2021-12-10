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

import xyz.xuminghai.pojo.entity.BaseItem;
import xyz.xuminghai.pojo.enums.CheckNameEnum;
import xyz.xuminghai.pojo.request.file.*;
import xyz.xuminghai.pojo.response.file.*;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 2021/11/1 17:26 星期一<br/>
 * 阻塞的执行器接口，继承执行器接口，重写返回值和自己的方法<br/>
 * 改执行器是阻塞的，所以等到的结果是不需要等待
 *
 * @author xuMingHai
 */
public interface BlockExecutor extends Executor {
    /**
     * 根据指定的请求参数，发送list请求
     *
     * @param listRequest list请求参数
     * @return 文件列表的响应
     */
    @Override
    ListResponse list(ListRequest listRequest);

    /**
     * 根据指定的搜索请求参数，发送search请求
     *
     * @param searchRequest 搜索请求参数
     * @return 搜索的响应
     */
    @Override
    SearchResponse search(SearchRequest searchRequest);

    /**
     * 根据文件ID，发送get请求
     *
     * @param fileId 文件ID
     * @return 这个文件的信息
     */
    @Override
    BaseItem get(String fileId);

    /**
     * 根据文件ID，获取文件的下载信息
     *
     * @param fileId 文件ID
     * @return 这个文件的下载信息
     */
    @Override
    DownloadUrlResponse getDownloadUrl(String fileId);


    /**
     * 下载文件
     *
     * @param fileId      文件ID
     * @param path        下载到的目标地址(不需要指定文件名)
     * @param openOptions 参数选项
     * @return 文件字节数
     */
    long downloadFile(String fileId, Path path, OpenOption... openOptions);

    /**
     * 根据创建文件夹的请求参数，发送创建文件夹的请求
     *
     * @param createFolderRequest 创建文件夹的请求参数
     * @return 返回的响应
     */
    @Override
    CreateFolderResponse createFolder(CreateFolderRequest createFolderRequest);

    /**
     * 上传文件
     *
     * @param parentFileId  父目录ID
     * @param path          要上传的文件路径
     * @param checkNameEnum 同名策略
     * @return 上传文件的响应
     */
    @Override
    CreateFileResponse uploadFile(String parentFileId, Path path, CheckNameEnum checkNameEnum);


    /**
     * 上传文件夹
     *
     * @param path          要上传的文件路径
     * @param parentFileId  父目录ID
     * @param checkNameEnum 同名策略
     * @return 上传文件夹的响应
     */
    default UploadFolderResponse uploadFolder(Path path, String parentFileId, CheckNameEnum checkNameEnum) {

        final UploadFolderResponse uploadFolderResponse = new UploadFolderResponse();
        try {
            Files.walkFileTree(path.toAbsolutePath(), new SimpleFileVisitor<Path>() {

                /**
                 * 当前正在上传的文件夹
                 */
                UploadFolderResponse.Folder currentFolder = new UploadFolderResponse.Folder();

                {
                    // 设置上传ID
                    currentFolder.setParentFileId(parentFileId);
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    // 要上传的文件夹为空，是第一次访问，初始化上传文件夹
                    if (Objects.isNull(uploadFolderResponse.getFolder())) {
                        uploadFolderResponse.setFolder(currentFolder);
                    } else {
                        // 如果上传文件夹不为空，则初始化一个新上传的文件夹
                        final UploadFolderResponse.Folder folder = new UploadFolderResponse.Folder();
                        // 设置当前文件夹是这个新的文件夹的父目录
                        folder.setParentFolder(currentFolder);
                        // 设置这个新建目录的父目录ID为当前目录的ID
                        folder.setParentFileId(currentFolder.getFileId());
                        // 当前文件夹添加这个新的文件夹
                        currentFolder.addFolder(folder);
                        // 将当前文件夹的引用改为这个新的文件夹
                        currentFolder = folder;
                    }

                    // 构建创建文件夹请求
                    final CreateFolderRequest createFolderRequest = new CreateFolderRequest();
                    createFolderRequest.setParentFileId(currentFolder.getParentFileId());
                    createFolderRequest.setName(dir.getFileName().toString());
                    createFolderRequest.setCheckNameMode(checkNameEnum);

                    // 创建文件夹，将响应的值赋值到当前文件夹
                    final CreateFolderResponse createFolderResponse = createFolder(createFolderRequest);
                    currentFolder.setFileName(createFolderResponse.getFileName());
                    currentFolder.setFileId(createFolderResponse.getFileId());
                    currentFolder.setPath(dir);

                    // 继续遍历文件树
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) {

                    // 初始化上传文件的响应，上传文件的时间开始初始化
                    final UploadFolderResponse.File file = new UploadFolderResponse.File();
                    // 这个新的文件的父目录是当前文件夹
                    file.setParentFolder(currentFolder);

                    // 开始上传文件，父目录ID为当前文件夹ID
                    final CreateFileResponse createFileResponse = uploadFile(currentFolder.getFileId(), filePath, checkNameEnum);

                    // 填充响应
                    file.setParentFileId(createFileResponse.getParentFileId());
                    file.setFileId(createFileResponse.getFileId());
                    file.setFileName(createFileResponse.getFileName());
                    file.setRapidUpload(createFileResponse.isRapidUpload());
                    file.setPath(filePath);
                    file.setUploadEndTime(LocalDateTime.now());

                    // 将这个文件响应填入当前文件夹
                    currentFolder.addFile(file);

                    // 继续遍历文件树
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    // 遍历完当前文件夹之后
                    // 设置文件夹上传结束时间
                    currentFolder.setUploadEndTime(LocalDateTime.now());

                    // 将当前文件夹的引用改为他的父文件夹的引用
                    currentFolder = currentFolder.getParentFolder();
                    return super.postVisitDirectory(dir, exc);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadFolderResponse;
    }

    /**
     * 根据指定的修改文件名参数，发送修改文件名请求
     *
     * @param updateRequest 修改文件名的请求参数
     * @return 返回的响应
     */
    @Override
    BaseItem update(UpdateRequest updateRequest);

    /**
     * 将文件或文件夹移入回收站
     *
     * @param fileId 文件ID
     * @return 返回的响应
     */
    @Override
    Boolean trash(String fileId);

    /**
     * 获取视频播放的预览信息
     * @param videoPreviewPlayInfoRequest 请求体
     * @return 返回的响应
     */
    @Override
    VideoPreviewPlayInfoResponse getVideoPreviewPlayInfo(VideoPreviewPlayInfoRequest videoPreviewPlayInfoRequest);

    /**
     * 获取播放音频信息
     * @param fileId 文件ID
     * @return 响应后的操作
     */
    @Override
    AudioPlayInfoResponse getAudioPlayInfo(String fileId);
}
