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

package xyz.xuminghai.template;

import xyz.xuminghai.cache.Cache;
import xyz.xuminghai.cache.NullCache;
import xyz.xuminghai.core.ReactiveFileDao;
import xyz.xuminghai.core.ReactiveRecycleDao;
import xyz.xuminghai.executor.BlockBaseExecutor;
import xyz.xuminghai.executor.BlockCacheExecutor;
import xyz.xuminghai.executor.BlockExecutor;
import xyz.xuminghai.pojo.entity.BaseItem;
import xyz.xuminghai.pojo.enums.CategoryEnum;
import xyz.xuminghai.pojo.enums.CheckNameEnum;
import xyz.xuminghai.pojo.enums.OrderByEnum;
import xyz.xuminghai.pojo.enums.OrderDirectionEnum;
import xyz.xuminghai.pojo.request.file.*;
import xyz.xuminghai.pojo.response.file.*;

import java.nio.file.OpenOption;
import java.nio.file.Path;

/**
 * 2021/10/26 15:04 星期二<br/>
 * 阻塞式客户端，适用于直接获取响应结果，对结果进行操作
 *
 * @author xuMingHai
 */
public class BlockClientTemplate extends AbstractTemplate {

    /**
     * 阻塞执行器
     */
    private final BlockExecutor blockExecutor;

    public BlockClientTemplate(Cache cache, ReactiveFileDao reactiveFileDao, ReactiveRecycleDao reactiveRecycleDao) {
        super(cache);
        final BlockBaseExecutor blockBaseExecutor = new BlockBaseExecutor(reactiveFileDao, reactiveRecycleDao);
        if (NullCache.NULL_CACHE.equals(cache.getName())) {
            this.blockExecutor = blockBaseExecutor;
        } else {
            this.blockExecutor = new BlockCacheExecutor(cache, blockBaseExecutor);
        }
    }


    /**
     * 获取顶级目录的文件列表<br/>
     * 顶级目录：{@link AbstractTemplate#DEFAULT_TOP_DIRECTORY}
     *
     * @return 顶级目录下的文件列表
     */
    @Override
    public ListResponse list() {
        return (ListResponse) super.list();
    }

    /**
     * 根据文件夹ID查询这个文件夹下的文件信息<br/>
     * 详细请求配置请参考：{@link ListRequest}
     *
     * @param folderId 文件夹ID
     * @return 文件列表响应
     */
    @Override
    public ListResponse list(String folderId) {
        return (ListResponse) super.list(folderId);
    }

    /**
     * 根据文件夹ID，查询这个文件夹下的文件信息<br/>
     * 详细请求配置请参考：{@link ListRequest}
     *
     * @param folderId    文件夹ID
     * @param orderByEnum 根据什么排序，如名称、创建时间。。。
     * @return 文件列表响应
     */
    @Override
    public ListResponse list(String folderId, OrderByEnum orderByEnum) {
        return (ListResponse) super.list(folderId, orderByEnum);
    }

    /**
     * 根据文件夹ID，查询这个文件夹下的文件信息<br/>
     * 详细请求配置请参考：{@link ListRequest}
     *
     * @param folderId           文件夹ID
     * @param orderDirectionEnum 排序方向，升序排列或降序
     * @return 文件列表响应
     */
    @Override
    public ListResponse list(String folderId, OrderDirectionEnum orderDirectionEnum) {
        return (ListResponse) super.list(folderId, orderDirectionEnum);
    }

    /**
     * 根据文件夹ID，查询这个文件夹下的文件信息<br/>
     * 详细请求配置请参考：{@link ListRequest}
     *
     * @param folderId           文件夹ID
     * @param orderByEnum        根据什么排序，如名称、创建时间。。。
     * @param orderDirectionEnum 排序方向，升序排列或降序
     * @return 文件列表响应
     */
    @Override
    public ListResponse list(String folderId, OrderByEnum orderByEnum, OrderDirectionEnum orderDirectionEnum) {
        return (ListResponse) super.list(folderId, orderByEnum, orderDirectionEnum);
    }

    /**
     * 自定义请求参数的文件列表获取
     *
     * @param listRequest 文件请求参数
     * @return 文件列表响应
     */
    @Override
    public ListResponse list(ListRequest listRequest) {
        return blockExecutor.list(listRequest);
    }

    /**
     * 根据文件名搜索所有类型的文件<br/>
     * 详细的请求配置请参考：{@link SearchRequest}
     *
     * @param fileName 文件名
     * @return 搜索响应结果。
     */
    @Override
    public SearchResponse search(String fileName) {
        return (SearchResponse) super.search(fileName);
    }

    /**
     * 根据文件名搜索指定类型的文件<br/>
     * 详细的请求配置请参考：{@link SearchRequest}
     *
     * @param fileName     文件名
     * @param categoryEnum 搜索的文件类型，如，图片、视频。。。
     * @return 搜索响应结果。
     */
    @Override
    public SearchResponse search(String fileName, CategoryEnum categoryEnum) {
        return (SearchResponse) super.search(fileName, categoryEnum);
    }

    /**
     * 根据文件名搜索文件<br/>
     * 详细的请求配置请参考：{@link SearchRequest}
     *
     * @param fileName           文件名
     * @param orderByEnum        根据什么排序，如名称、创建时间。。。
     * @param orderDirectionEnum 排序方向，升序排列或降序
     * @return 搜索响应结果。
     */
    @Override
    public SearchResponse search(String fileName, OrderByEnum orderByEnum, OrderDirectionEnum orderDirectionEnum) {
        return (SearchResponse) super.search(fileName, orderByEnum, orderDirectionEnum);
    }

    /**
     * 根据文件名搜索文件<br/>
     * 详细的请求配置请参考：{@link SearchRequest}
     *
     * @param fileName           文件名
     * @param categoryEnum       搜索的文件类型，如，图片、视频。。。
     * @param orderByEnum        根据什么排序，如名称、创建时间。。。
     * @param orderDirectionEnum 排序方向，如降序、升序
     * @return 搜索响应结果
     */
    @Override
    public SearchResponse search(String fileName, CategoryEnum categoryEnum, OrderByEnum orderByEnum, OrderDirectionEnum orderDirectionEnum) {
        return (SearchResponse) super.search(fileName, categoryEnum, orderByEnum, orderDirectionEnum);
    }

    /**
     * 使用自定义的请求参数搜索文件
     *
     * @param searchRequest 自定义搜索请求参数
     * @return 搜索响应结果
     */
    @Override
    public SearchResponse search(SearchRequest searchRequest) {
        return blockExecutor.search(searchRequest);
    }

    /**
     * 获取单个文件或文件夹的信息
     *
     * @param fileId 文件ID
     * @return 文件信息（可以根据类型转为对应的子类）
     */
    @Override
    public BaseItem get(String fileId) {
        return blockExecutor.get(fileId);
    }

    /**
     * 获取这个文件的下载参数
     *
     * @param fileId 文件ID
     * @return 这个文件的下载参数
     */
    @Override
    public DownloadUrlResponse getDownloadUrl(String fileId) {
        return blockExecutor.getDownloadUrl(fileId);
    }

    /**
     * 快速下载文件
     *
     * @param fileId      文件ID
     * @param path        下载到的目标地址(不需要指定文件名)
     * @param openOptions 参数选项
     * @return 文件字节数
     */
    public long downloadFile(String fileId, Path path, OpenOption... openOptions) {
        return blockExecutor.downloadFile(fileId, path, openOptions);
    }

    /**
     * 创建文件夹，使用默认的同名策略，自动重命名，使用默认的顶级目录<br/>
     * 详细的请求参数请参考：{@link CreateFolderRequest}
     *
     * @param name 文件夹名
     * @return 响应信息
     */
    @Override
    public CreateFolderResponse createFolder(String name) {
        return (CreateFolderResponse) super.createFolder(name);
    }

    /**
     * 创建文件夹，使用默认的同名策略，自动重命名<br/>
     * 详细的请求参数请参考：{@link CreateFolderRequest}
     *
     * @param parentFileId 父目录ID
     * @param name         文件夹名
     * @return 响应信息
     */
    @Override
    public CreateFolderResponse createFolder(String parentFileId, String name) {
        return (CreateFolderResponse) super.createFolder(parentFileId, name);
    }

    /**
     * 创建文件夹，在默认的顶级目录<br/>
     * 详细的请求参数请参考：{@link CreateFolderRequest}
     *
     * @param name          文件夹名
     * @param checkNameEnum 同名策略
     * @return 响应信息
     */
    @Override
    public CreateFolderResponse createFolder(String name, CheckNameEnum checkNameEnum) {
        return (CreateFolderResponse) super.createFolder(name, checkNameEnum);
    }

    /**
     * 创建文件夹<br/>
     * 详细的请求参数请参考：{@link CreateFolderRequest}
     *
     * @param parentFileId  父目录ID
     * @param name          文件夹名
     * @param checkNameEnum 同名策略
     * @return 响应信息
     */
    @Override
    public CreateFolderResponse createFolder(String parentFileId, String name, CheckNameEnum checkNameEnum) {
        return (CreateFolderResponse) super.createFolder(parentFileId, name, checkNameEnum);
    }

    /**
     * 创建文件夹
     *
     * @param createFolderRequest 创建文件夹请求
     * @return 响应信息
     */
    @Override
    public CreateFolderResponse createFolder(CreateFolderRequest createFolderRequest) {
        return blockExecutor.createFolder(createFolderRequest);
    }

    /**
     * 上传文件，使用默认的顶级目录、同名策略（支持快传和分片上传）
     *
     * @param path 文件路径
     * @return 响应信息
     */
    @Override
    public CreateFileResponse uploadFile(Path path) {
        return (CreateFileResponse) super.uploadFile(path);
    }

    /**
     * 上传文件，使用默认的同名策略（支持快传和分片上传）
     *
     * @param parentFileId 父目录ID
     * @param path         文件路径
     * @return 响应信息
     */
    @Override
    public CreateFileResponse uploadFile(String parentFileId, Path path) {
        return (CreateFileResponse) super.uploadFile(parentFileId, path);
    }

    /**
     * 上传文件，使用默认的顶级目录（支持快传和分片上传）
     *
     * @param path          文件路径
     * @param checkNameEnum 同名策略
     * @return 响应信息
     */
    @Override
    public CreateFileResponse uploadFile(Path path, CheckNameEnum checkNameEnum) {
        return (CreateFileResponse) super.uploadFile(path, checkNameEnum);
    }

    /**
     * 上传文件（支持快传和分片上传）
     *
     * @param path          文件路径
     * @param parentFileId  父目录ID
     * @param checkNameEnum 同名策略
     * @return 响应信息
     */
    @Override
    public CreateFileResponse uploadFile(String parentFileId, Path path, CheckNameEnum checkNameEnum) {
        return blockExecutor.uploadFile(parentFileId, path, checkNameEnum);
    }

    /**
     * 上传文件夹
     *
     * @param path 文件夹路径
     * @return 上传文件夹的响应
     */
    public UploadFolderResponse uploadFolder(Path path) {
        return uploadFolder(DEFAULT_TOP_DIRECTORY, path);
    }

    /**
     * 上传文件夹
     *
     * @param path         文件夹路径
     * @param parentFileId 父文件ID
     * @return 上传文件夹的响应
     */
    public UploadFolderResponse uploadFolder(String parentFileId, Path path) {
        return uploadFolder(parentFileId, path, CheckNameEnum.AUTO_RENAME);
    }

    /**
     * 上传文件夹
     *
     * @param path          文件夹路径
     * @param checkNameEnum 同名策略
     * @return 上传文件夹的响应
     */
    public UploadFolderResponse uploadFolder(Path path, CheckNameEnum checkNameEnum) {
        return uploadFolder(DEFAULT_TOP_DIRECTORY, path, checkNameEnum);
    }

    /**
     * 上传文件夹
     *
     * @param parentFileId  父文件ID
     * @param path          文件夹路径
     * @param checkNameEnum 同名策略
     * @return 上传文件夹的响应
     */
    public UploadFolderResponse uploadFolder(String parentFileId, Path path, CheckNameEnum checkNameEnum) {
        return blockExecutor.uploadFolder(path, parentFileId, checkNameEnum);
    }

    /**
     * 修改文件名或文件夹名
     *
     * @param fileId      文件ID
     * @param newFileName 新的文件名
     * @return 响应信息
     */
    @Override
    public BaseItem update(String fileId, String newFileName) {
        return (BaseItem) super.update(fileId, newFileName);
    }

    /**
     * 修改文件名或文件夹名
     *
     * @param updateRequest 修改文件名请求参数
     * @return 响应信息
     */
    @Override
    public BaseItem update(UpdateRequest updateRequest) {
        return blockExecutor.update(updateRequest);
    }

    /**
     * 将文件或文件夹移入回收站
     *
     * @param fileId 文件ID
     * @return 响应信息
     */
    @Override
    public Boolean trash(String fileId) {
        return blockExecutor.trash(fileId);
    }

    /**
     * 获取视频播放的预览信息
     *
     * @param videoPreviewPlayInfoRequest 请求体
     * @return 返回的响应
     */
    @Override
    public VideoPreviewPlayInfoResponse getVideoPreviewPlayInfo(VideoPreviewPlayInfoRequest videoPreviewPlayInfoRequest) {
        return blockExecutor.getVideoPreviewPlayInfo(videoPreviewPlayInfoRequest);
    }

    /**
     * 获取视频播放的预览信息
     *
     * @param fileId 文件ID
     * @return 响应信息
     */
    @Override
    public VideoPreviewPlayInfoResponse getVideoPreviewPlayInfo(String fileId) {
        return (VideoPreviewPlayInfoResponse) super.getVideoPreviewPlayInfo(fileId);
    }
}
