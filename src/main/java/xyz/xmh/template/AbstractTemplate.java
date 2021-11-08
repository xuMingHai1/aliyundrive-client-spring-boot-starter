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

package xyz.xmh.template;

import xyz.xmh.cache.Cache;
import xyz.xmh.pojo.enums.CategoryEnum;
import xyz.xmh.pojo.enums.CheckNameEnum;
import xyz.xmh.pojo.enums.OrderByEnum;
import xyz.xmh.pojo.enums.OrderDirectionEnum;
import xyz.xmh.pojo.request.file.CreateFolderRequest;
import xyz.xmh.pojo.request.file.ListRequest;
import xyz.xmh.pojo.request.file.SearchRequest;
import xyz.xmh.pojo.request.file.UpdateRequest;

import java.nio.file.Path;

/**
 * 2021/10/31 23:01 星期日<br/>
 * 抽象的客户端模板类，定义了一些共有的属性，和方法或行为
 *
 * @author xuMingHai
 */
abstract class AbstractTemplate {

    /**
     * 阿里云盘默认的顶级目录
     */
    protected static final String DEFAULT_TOP_DIRECTORY = "root";

    /**
     * 共有的缓存实例
     */
    protected final Cache cache;

    protected AbstractTemplate(Cache cache) {
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
    protected Object list() {
        return list(new ListRequest());
    }

    /**
     * 根据文件夹ID查询这个文件夹下的文件信息<br/>
     * 详细请求配置请参考：{@link ListRequest}
     *
     * @param folderId 文件夹ID
     * @return 文件列表响应
     */
    protected Object list(String folderId) {
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
    protected Object list(String folderId, OrderByEnum orderByEnum) {
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
    protected Object list(String folderId, OrderDirectionEnum orderDirectionEnum) {
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
    protected Object list(String folderId, OrderByEnum orderByEnum, OrderDirectionEnum orderDirectionEnum) {
        final ListRequest listRequest = new ListRequest();
        listRequest.setParentFileId(folderId);
        listRequest.setOrderBy(orderByEnum);
        listRequest.setOrderDirection(orderDirectionEnum);
        return list(listRequest);
    }

    /**
     * 自定义请求参数的文件列表获取
     *
     * @param listRequest 文件请求参数
     * @return 文件列表响应
     */
    protected abstract Object list(ListRequest listRequest);

    /**
     * 根据文件名搜索文件<br/>
     * 详细的请求配置请参考：{@link SearchRequest}
     *
     * @param fileName 文件名
     * @return 搜索响应结果。
     */
    protected Object search(String fileName) {
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
    protected Object search(String fileName, CategoryEnum categoryEnum) {
        return search(fileName, categoryEnum, OrderByEnum.UPDATE_AT, OrderDirectionEnum.DESC);
    }

    /**
     * 根据文件名搜索文件<br/>
     * 详细的请求配置请参考：{@link SearchRequest}
     *
     * @param fileName     文件名
     * @param orderByEnum        根据什么排序，如名称、创建时间。。。
     * @param orderDirectionEnum 排序方向，升序排列或降序
     * @return 搜索响应结果。
     */
    protected Object search(String fileName, OrderByEnum orderByEnum, OrderDirectionEnum orderDirectionEnum) {
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
    protected Object search(String fileName, CategoryEnum categoryEnum, OrderByEnum orderByEnum, OrderDirectionEnum orderDirectionEnum) {
        final SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQuery(fileName, categoryEnum);
        searchRequest.setOrderBy(orderByEnum, orderDirectionEnum);
        return search(searchRequest);
    }

    /**
     * 使用自定义的请求参数搜索文件
     *
     * @param searchRequest 自定义搜索请求参数
     * @return 搜索响应结果
     */
    protected abstract Object search(SearchRequest searchRequest);

    /**
     * 获取单个文件或文件夹的信息
     *
     * @param fileId 文件ID
     * @return 文件信息（可以根据类型转为对应的子类）
     */
    protected abstract Object get(String fileId);

    /**
     * 获取这个文件的下载参数
     *
     * @param fileId 文件ID
     * @return 这个文件的下载参数
     */
    protected abstract Object getDownloadUrl(String fileId);

    /**
     * 创建文件夹，使用默认的同名策略，自动重命名，使用默认的顶级目录<br/>
     * 详细的请求参数请参考：{@link CreateFolderRequest}
     *
     * @param name 文件夹名
     * @return 响应信息
     */
    protected Object createFolder(String name) {
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
    protected Object createFolder(String parentFileId, String name) {
        return createFolder(parentFileId, name, CheckNameEnum.AUTO_RENAME);
    }

    /**
     * 创建文件夹，在默认的顶级目录<br/>
     * 详细的请求参数请参考：{@link CreateFolderRequest}
     *
     * @param name         文件夹名
     * @param checkNameEnum 同名策略
     * @return 响应信息
     */
    protected Object createFolder(String name, CheckNameEnum checkNameEnum) {
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
    protected Object createFolder(String parentFileId, String name, CheckNameEnum checkNameEnum) {
        final CreateFolderRequest createFolderRequest = new CreateFolderRequest();
        createFolderRequest.setParentFileId(parentFileId);
        createFolderRequest.setName(name);
        createFolderRequest.setCheckNameMode(checkNameEnum);
        return createFolder(createFolderRequest);
    }

    /**
     * 创建文件夹
     *
     * @param createFolderRequest 创建文件夹请求
     * @return 响应信息
     */
    protected abstract Object createFolder(CreateFolderRequest createFolderRequest);

    /**
     * 上传文件，使用默认的顶级目录、同名策略（支持快传和分片上传）
     *
     * @param path 文件路径
     * @return 响应信息
     */
    protected Object uploadFile(Path path) {
        return uploadFile(path, DEFAULT_TOP_DIRECTORY);
    }

    /**
     * 上传文件，使用默认的同名策略（支持快传和分片上传）
     *
     * @param path         文件路径
     * @param parentFileId 父目录ID
     * @return 响应信息
     */
    protected Object uploadFile(Path path, String parentFileId) {
        return uploadFile(path, parentFileId, CheckNameEnum.AUTO_RENAME);
    }

    /**
     * 上传文件，使用默认的顶级目录（支持快传和分片上传）
     *
     * @param path         文件路径
     * @param checkNameEnum 同名策略
     * @return 响应信息
     */
    protected Object uploadFile(Path path, CheckNameEnum checkNameEnum) {
        return uploadFile(path, DEFAULT_TOP_DIRECTORY, checkNameEnum);
    }

    /**
     * 上传文件（支持快传和分片上传）
     *
     * @param path          文件路径
     * @param parentFileId  父目录ID
     * @param checkNameEnum 同名策略
     * @return 响应信息
     */
    protected abstract Object uploadFile(Path path, String parentFileId, CheckNameEnum checkNameEnum);

    /**
     * 修改文件名
     *
     * @param fileId      文件ID
     * @param newFileName 新的文件名
     * @return 响应信息
     */
    protected Object update(String fileId, String newFileName) {
        return update(new UpdateRequest(fileId, newFileName));
    }

    /**
     * 修改文件名
     *
     * @param updateRequest 修改文件名请求参数
     * @return 响应信息
     */
    protected abstract Object update(UpdateRequest updateRequest);

    /**
     * 将文件移入回收站
     *
     * @param fileId 文件ID
     * @return 响应信息
     */
    protected abstract Object trash(String fileId);

}
