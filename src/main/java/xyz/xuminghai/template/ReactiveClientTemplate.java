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

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import xyz.xuminghai.cache.Cache;
import xyz.xuminghai.cache.NullCache;
import xyz.xuminghai.core.ReactiveFileDao;
import xyz.xuminghai.core.ReactiveRecycleDao;
import xyz.xuminghai.executor.ReactiveBaseExecutor;
import xyz.xuminghai.executor.ReactiveCacheExecutor;
import xyz.xuminghai.executor.ReactiveExecutor;
import xyz.xuminghai.pojo.entity.BaseItem;
import xyz.xuminghai.pojo.enums.CategoryEnum;
import xyz.xuminghai.pojo.enums.CheckNameEnum;
import xyz.xuminghai.pojo.enums.OrderByEnum;
import xyz.xuminghai.pojo.enums.OrderDirectionEnum;
import xyz.xuminghai.pojo.request.file.CreateFolderRequest;
import xyz.xuminghai.pojo.request.file.ListRequest;
import xyz.xuminghai.pojo.request.file.SearchRequest;
import xyz.xuminghai.pojo.request.file.UpdateRequest;
import xyz.xuminghai.pojo.response.file.*;

import java.nio.file.Path;

/**
 * 2021/10/26 14:56 星期二<br/>
 * 反应式的客户端模板，适用于直接返回http实体，这个模板下的方法是异步的，并不会直接得到结果
 *
 * @author xuMingHai
 */
@SuppressWarnings("unchecked")
public class ReactiveClientTemplate extends AbstractTemplate {

    /**
     * 响应式客户端
     */
    private final ReactiveExecutor reactiveExecutor;

    public ReactiveClientTemplate(Cache cache, ReactiveFileDao reactiveFileDao, ReactiveRecycleDao reactiveRecycleDao) {
        super(cache);
        final ReactiveBaseExecutor reactiveBaseExecutor = new ReactiveBaseExecutor(reactiveFileDao, reactiveRecycleDao);
        if (NullCache.NULL_CACHE.equals(cache.getName())) {
            this.reactiveExecutor = reactiveBaseExecutor;
        } else {
            this.reactiveExecutor = new ReactiveCacheExecutor(cache, reactiveBaseExecutor);
        }
    }

    /**
     * 获取顶级目录的文件列表<br/>
     * 顶级目录：{@link AbstractTemplate#DEFAULT_TOP_DIRECTORY}
     *
     * @return 顶级目录下的文件列表
     */
    @Override
    public Mono<ResponseEntity<ListResponse>> list() {
        return (Mono<ResponseEntity<ListResponse>>) super.list();
    }

    /**
     * 根据文件夹ID查询这个文件夹下的文件信息<br/>
     * 详细请求配置请参考：{@link ListRequest}
     *
     * @param folderId 文件夹ID
     * @return 文件列表响应
     */
    @Override
    public Mono<ResponseEntity<ListResponse>> list(String folderId) {
        return (Mono<ResponseEntity<ListResponse>>) super.list(folderId);
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
    public Mono<ResponseEntity<ListResponse>> list(String folderId, OrderByEnum orderByEnum) {
        return (Mono<ResponseEntity<ListResponse>>) super.list(folderId, orderByEnum);
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
    public Mono<ResponseEntity<ListResponse>> list(String folderId, OrderDirectionEnum orderDirectionEnum) {
        return (Mono<ResponseEntity<ListResponse>>) super.list(folderId, orderDirectionEnum);
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
    public Mono<ResponseEntity<ListResponse>> list(String folderId, OrderByEnum orderByEnum, OrderDirectionEnum orderDirectionEnum) {
        return (Mono<ResponseEntity<ListResponse>>) super.list(folderId, orderByEnum, orderDirectionEnum);
    }

    /**
     * 自定义请求参数的文件列表获取
     *
     * @param listRequest 文件请求参数
     * @return 文件列表响应
     */
    @Override
    public Mono<ResponseEntity<ListResponse>> list(ListRequest listRequest) {
        return reactiveExecutor.list(listRequest);
    }

    /**
     * 根据文件名搜索文件<br/>
     * 详细的请求配置请参考：{@link SearchRequest}
     *
     * @param fileName 文件名
     * @return 搜索响应结果。
     */
    @Override
    public Mono<ResponseEntity<SearchResponse>> search(String fileName) {
        return (Mono<ResponseEntity<SearchResponse>>) super.search(fileName);
    }

    /**
     * 根据文件名搜索文件<br/>
     * 详细的请求配置请参考：{@link SearchRequest}
     *
     * @param fileName     文件名
     * @param categoryEnum 搜索的文件类型，如，图片、视频。。。
     * @return 搜索响应结果。
     */
    @Override
    public Mono<ResponseEntity<SearchResponse>> search(String fileName, CategoryEnum categoryEnum) {
        return (Mono<ResponseEntity<SearchResponse>>) super.search(fileName, categoryEnum);
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
    public Mono<ResponseEntity<SearchResponse>> search(String fileName, OrderByEnum orderByEnum, OrderDirectionEnum orderDirectionEnum) {
        return (Mono<ResponseEntity<SearchResponse>>) super.search(fileName, orderByEnum, orderDirectionEnum);
    }

    /**
     * 根据文件名搜索文件<br/>
     * 详细的请求配置请参考：{@link SearchRequest}
     *
     * @param fileName           文件名
     * @param categoryEnum       搜索的文件类型，如，图片、视频。。。
     * @param orderByEnum        根据什么排序，如名称、创建时间。。。
     * @param orderDirectionEnum 排序方向，升序排列或降序
     * @return 搜索响应结果
     */
    @Override
    public Mono<ResponseEntity<SearchResponse>> search(String fileName, CategoryEnum categoryEnum, OrderByEnum orderByEnum, OrderDirectionEnum orderDirectionEnum) {
        return (Mono<ResponseEntity<SearchResponse>>) super.search(fileName, categoryEnum, orderByEnum, orderDirectionEnum);
    }

    /**
     * 使用自定义的请求参数搜索文件
     *
     * @param searchRequest 自定义搜索请求参数
     * @return 搜索响应结果
     */
    @Override
    public Mono<ResponseEntity<SearchResponse>> search(SearchRequest searchRequest) {
        return reactiveExecutor.search(searchRequest);
    }

    /**
     * 获取单个文件或文件夹的信息
     *
     * @param fileId 文件ID
     * @return 文件信息（可以根据类型转为对应的子类）
     */
    @Override
    public Mono<ResponseEntity<BaseItem>> get(String fileId) {
        return reactiveExecutor.get(fileId);
    }

    /**
     * 获取这个文件的下载参数
     *
     * @param fileId 文件ID
     * @return 这个文件的下载参数
     */
    @Override
    public Mono<ResponseEntity<DownloadUrlResponse>> getDownloadUrl(String fileId) {
        return reactiveExecutor.getDownloadUrl(fileId);
    }

    /**
     * 下载文件，支持分段下载，断点续传
     *
     * @param fileId      文件ID
     * @param httpHeaders 表示 HTTP 请求头
     * @return Http异步响应体
     */
    public Mono<ResponseEntity<Resource>> downloadFile(String fileId, HttpHeaders httpHeaders) {
        return reactiveExecutor.downloadFile(fileId, httpHeaders);
    }

    /**
     * 创建文件夹，使用默认的同名策略，自动重命名，使用默认的顶级目录<br/>
     * 详细的请求参数请参考：{@link CreateFolderRequest}
     *
     * @param name 文件夹名
     * @return 响应信息
     */
    @Override
    public Mono<ResponseEntity<CreateFolderResponse>> createFolder(String name) {
        return (Mono<ResponseEntity<CreateFolderResponse>>) super.createFolder(name);
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
    public Mono<ResponseEntity<CreateFolderResponse>> createFolder(String parentFileId, String name) {
        return (Mono<ResponseEntity<CreateFolderResponse>>) super.createFolder(parentFileId, name);
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
    public Mono<ResponseEntity<CreateFolderResponse>> createFolder(String name, CheckNameEnum checkNameEnum) {
        return (Mono<ResponseEntity<CreateFolderResponse>>) super.createFolder(name, checkNameEnum);
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
    public Mono<ResponseEntity<CreateFolderResponse>> createFolder(String parentFileId, String name, CheckNameEnum checkNameEnum) {
        return (Mono<ResponseEntity<CreateFolderResponse>>) super.createFolder(parentFileId, name, checkNameEnum);
    }

    /**
     * 创建文件夹
     *
     * @param createFolderRequest 创建文件夹请求
     * @return 响应信息
     */
    @Override
    public Mono<ResponseEntity<CreateFolderResponse>> createFolder(CreateFolderRequest createFolderRequest) {
        return reactiveExecutor.createFolder(createFolderRequest);
    }

    /**
     * 上传文件，使用默认的顶级目录、同名策略（支持快传和分片上传）
     *
     * @param path 文件路径
     * @return 响应信息
     */
    @Override
    public Mono<CreateFileResponse> uploadFile(Path path) {
        return (Mono<CreateFileResponse>) super.uploadFile(path);
    }

    /**
     * 上传文件，使用默认的同名策略（支持快传和分片上传）
     *
     * @param path         文件路径
     * @param parentFileId 父目录ID
     * @return 响应信息
     */
    @Override
    public Mono<CreateFileResponse> uploadFile(String parentFileId, Path path) {
        return (Mono<CreateFileResponse>) super.uploadFile(parentFileId, path);
    }

    /**
     * 上传文件，使用默认的顶级目录（支持快传和分片上传）
     *
     * @param path          文件路径
     * @param checkNameEnum 同名策略
     * @return 响应信息
     */
    @Override
    public Mono<CreateFileResponse> uploadFile(Path path, CheckNameEnum checkNameEnum) {
        return (Mono<CreateFileResponse>) super.uploadFile(path, checkNameEnum);
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
    public Mono<CreateFileResponse> uploadFile(String parentFileId, Path path, CheckNameEnum checkNameEnum) {
        return reactiveExecutor.uploadFile(parentFileId, path, checkNameEnum);
    }

    /**
     * 修改文件名
     *
     * @param fileId      文件ID
     * @param newFileName 新的文件名
     * @return 响应信息
     */
    @Override
    public Mono<ResponseEntity<BaseItem>> update(String fileId, String newFileName) {
        return (Mono<ResponseEntity<BaseItem>>) super.update(fileId, newFileName);
    }

    /**
     * 修改文件名
     *
     * @param updateRequest 修改文件名请求参数
     * @return 响应信息
     */
    @Override
    public Mono<ResponseEntity<BaseItem>> update(UpdateRequest updateRequest) {
        return reactiveExecutor.update(updateRequest);
    }

    /**
     * 将文件移入回收站
     *
     * @param fileId 文件ID
     * @return 响应信息
     */
    @Override
    public Mono<ResponseEntity<Void>> trash(String fileId) {
        return reactiveExecutor.trash(fileId);
    }
}
