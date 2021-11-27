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
import xyz.xuminghai.executor.Executor;
import xyz.xuminghai.pojo.enums.CategoryEnum;
import xyz.xuminghai.pojo.enums.CheckNameEnum;
import xyz.xuminghai.pojo.enums.OrderByEnum;
import xyz.xuminghai.pojo.enums.OrderDirectionEnum;
import xyz.xuminghai.pojo.request.file.*;

import java.nio.file.Path;

/**
 * 2021/10/31 23:01 星期日<br/>
 * 抽象的客户端模板类，定义了一些共有的属性，和方法或行为
 *
 * @author xuMingHai
 */
abstract class AbstractTemplate implements Executor {

    /**
     * 阿里云盘默认的顶级目录
     */
    public static final String DEFAULT_TOP_DIRECTORY = "root";

    /**
     * 共有的缓存实例，因为只会创建一个缓存实例，所以这是模板类共有的缓存实例
     */
    private final Cache cache;

    AbstractTemplate(Cache cache) {
        this.cache = cache;
    }

    /**
     * 获取缓存实例
     *
     * @return 缓存实例，如果没有开启缓存会返回空对象缓存
     */
    public Cache getCache() {
        return cache;
    }

    /**
     * 获取顶级目录的文件列表<br/>
     * 顶级目录：{@link AbstractTemplate#DEFAULT_TOP_DIRECTORY}
     *
     * @return 顶级目录下的文件列表
     */
    public Object list() {
        return list(new ListRequest());
    }

    /**
     * 根据文件夹ID查询这个文件夹下的文件信息<br/>
     * 详细请求配置请参考：{@link ListRequest}
     *
     * @param folderId 文件夹ID
     * @return 文件列表响应
     */
    public Object list(String folderId) {
        return list(folderId, OrderByEnum.UPDATE_AT);
    }

    /**
     * 根据文件夹ID，查询这个文件夹下的文件信息<br/>
     * 详细请求配置请参考：{@link ListRequest}
     *
     * @param folderId    文件夹ID
     * @param orderByEnum 根据什么排序，如名称、创建时间。。。
     * @return 文件列表响应
     */
    public Object list(String folderId, OrderByEnum orderByEnum) {
        return list(folderId, orderByEnum, OrderDirectionEnum.DESC);
    }

    /**
     * 根据文件夹ID，查询这个文件夹下的文件信息<br/>
     * 详细请求配置请参考：{@link ListRequest}
     *
     * @param folderId           文件夹ID
     * @param orderDirectionEnum 排序方向，升序排列或降序
     * @return 文件列表响应
     */
    public Object list(String folderId, OrderDirectionEnum orderDirectionEnum) {
        return list(folderId, OrderByEnum.UPDATE_AT, orderDirectionEnum);
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
    public Object list(String folderId, OrderByEnum orderByEnum, OrderDirectionEnum orderDirectionEnum) {
        final ListRequest listRequest = new ListRequest();
        listRequest.setParentFileId(folderId);
        listRequest.setOrderBy(orderByEnum);
        listRequest.setOrderDirection(orderDirectionEnum);
        return list(listRequest);
    }

    /**
     * 根据文件名搜索文件<br/>
     * 详细的请求配置请参考：{@link SearchRequest}
     *
     * @param fileName 文件名
     * @return 搜索响应结果。
     */
    public Object search(String fileName) {
        return search(fileName, CategoryEnum.ALL);
    }

    /**
     * 根据文件名搜索文件<br/>
     * 详细的请求配置请参考：{@link SearchRequest}
     *
     * @param fileName     文件名
     * @param categoryEnum 搜索的文件类型，如，图片、视频。。。
     * @return 搜索响应结果。
     */
    public Object search(String fileName, CategoryEnum categoryEnum) {
        return search(fileName, categoryEnum, OrderByEnum.UPDATE_AT, OrderDirectionEnum.DESC);
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
    public Object search(String fileName, OrderByEnum orderByEnum, OrderDirectionEnum orderDirectionEnum) {
        return search(fileName, CategoryEnum.ALL, orderByEnum, orderDirectionEnum);
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
    public Object search(String fileName, CategoryEnum categoryEnum, OrderByEnum orderByEnum, OrderDirectionEnum orderDirectionEnum) {
        final SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQuery(fileName, categoryEnum);
        searchRequest.setOrderBy(orderByEnum, orderDirectionEnum);
        return search(searchRequest);
    }

    /**
     * 创建文件夹，使用默认的同名策略，自动重命名，使用默认的顶级目录<br/>
     * 详细的请求参数请参考：{@link CreateFolderRequest}
     *
     * @param name 文件夹名
     * @return 响应信息
     */
    public Object createFolder(String name) {
        return createFolder(DEFAULT_TOP_DIRECTORY, name);
    }

    /**
     * 创建文件夹，使用默认的同名策略，自动重命名<br/>
     * 详细的请求参数请参考：{@link CreateFolderRequest}
     *
     * @param parentFileId 父目录ID
     * @param name         文件夹名
     * @return 响应信息
     */
    public Object createFolder(String parentFileId, String name) {
        return createFolder(parentFileId, name, CheckNameEnum.AUTO_RENAME);
    }

    /**
     * 创建文件夹，在默认的顶级目录<br/>
     * 详细的请求参数请参考：{@link CreateFolderRequest}
     *
     * @param name          文件夹名
     * @param checkNameEnum 同名策略
     * @return 响应信息
     */
    public Object createFolder(String name, CheckNameEnum checkNameEnum) {
        return createFolder(DEFAULT_TOP_DIRECTORY, name, checkNameEnum);
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
    public Object createFolder(String parentFileId, String name, CheckNameEnum checkNameEnum) {
        final CreateFolderRequest createFolderRequest = new CreateFolderRequest();
        createFolderRequest.setParentFileId(parentFileId);
        createFolderRequest.setName(name);
        createFolderRequest.setCheckNameMode(checkNameEnum);
        return createFolder(createFolderRequest);
    }

    /**
     * 上传文件，使用默认的顶级目录、同名策略（支持快传和分片上传）
     *
     * @param path 文件路径
     * @return 响应信息
     */
    public Object uploadFile(Path path) {
        return uploadFile(DEFAULT_TOP_DIRECTORY, path);
    }

    /**
     * 上传文件，使用默认的同名策略（支持快传和分片上传）
     *
     * @param path         文件路径
     * @param parentFileId 父目录ID
     * @return 响应信息
     */
    public Object uploadFile(String parentFileId, Path path) {
        return uploadFile(parentFileId, path, CheckNameEnum.AUTO_RENAME);
    }

    /**
     * 上传文件，使用默认的顶级目录（支持快传和分片上传）
     *
     * @param path          文件路径
     * @param checkNameEnum 同名策略
     * @return 响应信息
     */
    public Object uploadFile(Path path, CheckNameEnum checkNameEnum) {
        return uploadFile(DEFAULT_TOP_DIRECTORY, path, checkNameEnum);
    }

    /**
     * 修改文件名或文件夹名
     *
     * @param fileId      文件ID
     * @param newFileName 新的文件名
     * @return 响应信息
     */
    public Object update(String fileId, String newFileName) {
        return update(new UpdateRequest(fileId, newFileName));
    }

    /**
     * 获取视频播放的预览信息
     * @param fileId 文件ID
     * @return 响应信息
     */
    public Object getVideoPreviewPlayInfo(String fileId) {
        return getVideoPreviewPlayInfo(new VideoPreviewPlayInfoRequest(fileId));
    }

}
