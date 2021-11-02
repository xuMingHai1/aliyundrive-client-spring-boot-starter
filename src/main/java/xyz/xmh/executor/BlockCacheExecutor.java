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

package xyz.xmh.executor;

import org.springframework.util.ObjectUtils;
import xyz.xmh.cache.Cache;
import xyz.xmh.pojo.entity.BaseItem;
import xyz.xmh.pojo.enums.CheckNameEnum;
import xyz.xmh.pojo.request.file.CreateFolderRequest;
import xyz.xmh.pojo.request.file.ListRequest;
import xyz.xmh.pojo.request.file.SearchRequest;
import xyz.xmh.pojo.request.file.UpdateRequest;
import xyz.xmh.pojo.response.file.*;

import java.nio.file.OpenOption;
import java.nio.file.Path;

/**
 * 2021/10/26 14:35 星期二<br/>
 * 阻塞执行器的缓存执行器
 *
 * @author xuMingHai
 */
public class BlockCacheExecutor implements BlockExecutor {

    private final Cache cache;

    private final BlockExecutor blockExecutor;

    public BlockCacheExecutor(Cache cache, BlockExecutor blockExecutor) {
        this.cache = cache;
        this.blockExecutor = blockExecutor;
    }

    @Override
    public ListResponse list(ListRequest listRequest) {
        final String key = "block_list_" + listRequest.hashCode();
        final Object o = cache.get(key);
        ListResponse listResponse;

        if (ObjectUtils.isEmpty(o)) {
            listResponse = blockExecutor.list(listRequest);
            cache.put(key, listResponse);
        } else {
            listResponse = (ListResponse) o;
        }

        return listResponse;
    }

    @Override
    public SearchResponse search(SearchRequest searchRequest) {
        final String key = "block_search_" + searchRequest.hashCode();
        final Object o = cache.get(key);
        SearchResponse searchResponse;

        if (ObjectUtils.isEmpty(o)) {
            searchResponse = blockExecutor.search(searchRequest);
            cache.put(key, searchResponse);
        } else {
            searchResponse = (SearchResponse) o;
        }

        return searchResponse;
    }

    @Override
    public BaseItem get(String fileId) {
        final String key = "block_get_" + fileId;
        final Object o = cache.get(key);
        BaseItem baseItem;

        if (ObjectUtils.isEmpty(o)) {
            baseItem = blockExecutor.get(fileId);
            cache.put(key, baseItem);
        } else {
            baseItem = (BaseItem) o;
        }

        return baseItem;
    }

    @Override
    public DownloadUrlResponse getDownloadUrl(String fileId) {
        return blockExecutor.getDownloadUrl(fileId);
    }

    @Override
    public long downloadFile(String fileId, Path path, OpenOption... openOptions) {
        return blockExecutor.downloadFile(fileId, path, openOptions);
    }

    @Override
    public CreateFolderResponse createFolder(CreateFolderRequest createFolderRequest) {
        cache.clear();
        return blockExecutor.createFolder(createFolderRequest);
    }

    @Override
    public CreateFileResponse uploadFile(String parentFileId, Path path, CheckNameEnum checkNameEnum) {
        cache.clear();
        return blockExecutor.uploadFile(parentFileId, path, checkNameEnum);
    }

    @Override
    public Boolean trash(String fileId) {
        cache.clear();
        return blockExecutor.trash(fileId);
    }

    @Override
    public BaseItem update(UpdateRequest updateRequest) {
        cache.clear();
        return blockExecutor.update(updateRequest);
    }

}
