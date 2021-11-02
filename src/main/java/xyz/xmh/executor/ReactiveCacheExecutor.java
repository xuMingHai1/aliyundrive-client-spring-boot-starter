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

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;
import xyz.xmh.cache.Cache;
import xyz.xmh.pojo.entity.BaseItem;
import xyz.xmh.pojo.enums.CheckNameEnum;
import xyz.xmh.pojo.request.file.CreateFolderRequest;
import xyz.xmh.pojo.request.file.ListRequest;
import xyz.xmh.pojo.request.file.SearchRequest;
import xyz.xmh.pojo.request.file.UpdateRequest;
import xyz.xmh.pojo.response.file.*;

import java.nio.file.Path;

/**
 * 2021/10/25 16:39 星期一<br/>
 * 反应性执行器的缓存执行器
 *
 * @author xuMingHai
 */
public class ReactiveCacheExecutor implements ReactiveExecutor {

    private final Cache cache;

    private final ReactiveExecutor reactiveExecutor;

    public ReactiveCacheExecutor(Cache cache, ReactiveExecutor reactiveExecutor) {
        this.cache = cache;
        this.reactiveExecutor = reactiveExecutor;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Mono<ResponseEntity<ListResponse>> list(ListRequest listRequest) {
        final String key = "reactive_list_" + listRequest.hashCode();
        final Object o = cache.get(key);
        Mono<ResponseEntity<ListResponse>> mono;

        if (ObjectUtils.isEmpty(o)) {
            mono = reactiveExecutor.list(listRequest).cache();
            cache.put(key, mono);
        } else {
            mono = (Mono<ResponseEntity<ListResponse>>) o;
        }

        return mono;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Mono<ResponseEntity<SearchResponse>> search(SearchRequest searchRequest) {
        final String key = "reactive_search_" + searchRequest.hashCode();
        final Object o = cache.get(key);
        Mono<ResponseEntity<SearchResponse>> mono;

        if (ObjectUtils.isEmpty(o)) {
            mono = reactiveExecutor.search(searchRequest).cache();
            cache.put(key, mono);
        } else {
            mono = (Mono<ResponseEntity<SearchResponse>>) o;
        }

        return mono;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mono<ResponseEntity<BaseItem>> get(String fileId) {
        final String key = "reactive_get_" + fileId;
        final Object o = cache.get(key);
        Mono<ResponseEntity<BaseItem>> mono;

        if (ObjectUtils.isEmpty(o)) {
            mono = reactiveExecutor.get(fileId).cache();
            cache.put(key, mono);
        } else {
            mono = (Mono<ResponseEntity<BaseItem>>) o;
        }

        return mono;
    }

    @Override
    public Mono<ResponseEntity<DownloadUrlResponse>> getDownloadUrl(String fileId) {
        return reactiveExecutor.getDownloadUrl(fileId);
    }

    @Override
    public Mono<ResponseEntity<Resource>> downloadFile(String fileId, HttpHeaders httpHeaders) {
        return reactiveExecutor.downloadFile(fileId, httpHeaders);
    }

    @Override
    public Mono<ResponseEntity<CreateFolderResponse>> createFolder(CreateFolderRequest createFolderRequest) {
        cache.clear();
        return reactiveExecutor.createFolder(createFolderRequest);
    }

    @Override
    public Mono<CreateFileResponse> uploadFile(String parentFileId, Path path, CheckNameEnum checkNameEnum) {
        cache.clear();
        return reactiveExecutor.uploadFile(parentFileId, path, checkNameEnum);
    }


    @Override
    public Mono<ResponseEntity<Void>> trash(String fileId) {
        cache.clear();
        return reactiveExecutor.trash(fileId);
    }

    @Override
    public Mono<ResponseEntity<BaseItem>> update(UpdateRequest updateRequest) {
        cache.clear();
        return reactiveExecutor.update(updateRequest);
    }

}
