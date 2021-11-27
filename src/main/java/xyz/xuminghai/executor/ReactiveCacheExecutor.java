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

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import xyz.xuminghai.cache.Cache;
import xyz.xuminghai.pojo.entity.BaseItem;
import xyz.xuminghai.pojo.enums.CheckNameEnum;
import xyz.xuminghai.pojo.request.file.*;
import xyz.xuminghai.pojo.response.file.*;

import java.net.URL;
import java.nio.file.Path;

/**
 * 2021/10/25 16:39 星期一<br/>
 * 反应性执行器的缓存执行器
 *
 * @author xuMingHai
 */
public class ReactiveCacheExecutor extends AbstractCacheExecutor implements ReactiveExecutor {

    private final ReactiveExecutor reactiveExecutor;

    public ReactiveCacheExecutor(Cache cache, ReactiveExecutor reactiveExecutor) {
        super(cache);
        this.reactiveExecutor = reactiveExecutor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mono<ResponseEntity<ListResponse>> list(ListRequest listRequest) {
        final String key = "reactive_list_" + listRequest.hashCode();
        final ResponseEntity<ListResponse> listResponseEntity = (ResponseEntity<ListResponse>) cache.get(key);
        return Mono.create(monoSink -> {
            if (listResponseEntity == null) {
                reactiveExecutor.list(listRequest)
                        .doOnNext(responseEntity -> cache.put(key, responseEntity))
                        .subscribe(monoSink::success, monoSink::error);
            } else {
                monoSink.success(listResponseEntity);
            }
        });
    }


    @SuppressWarnings("unchecked")
    @Override
    public Mono<ResponseEntity<SearchResponse>> search(SearchRequest searchRequest) {
        final String key = "reactive_search_" + searchRequest.hashCode();
        final ResponseEntity<SearchResponse> responseResponseEntity = (ResponseEntity<SearchResponse>) cache.get(key);
        return Mono.create(monoSink -> {
            if (responseResponseEntity == null) {
                reactiveExecutor.search(searchRequest)
                        .doOnNext(responseEntity -> cache.put(key, responseEntity))
                        .subscribe(monoSink::success, monoSink::error);
            } else {
                monoSink.success(responseResponseEntity);
            }
        });
    }


    @SuppressWarnings("unchecked")
    @Override
    public Mono<ResponseEntity<BaseItem>> get(String fileId) {
        final String key = "reactive_get_" + fileId;
        final ResponseEntity<BaseItem> getResponseEntity = (ResponseEntity<BaseItem>) cache.get(key);
        return Mono.create(monoSink -> {
            if (getResponseEntity == null) {
                reactiveExecutor.get(fileId)
                        .doOnNext(responseEntity -> cache.put(key, responseEntity))
                        .subscribe(monoSink::success, monoSink::error);
            } else {
                monoSink.success(getResponseEntity);
            }
        });
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

    @Override
    public Mono<ResponseEntity<VideoPreviewPlayInfoResponse>> getVideoPreviewPlayInfo(VideoPreviewPlayInfoRequest videoPreviewPlayInfoRequest) {
        return reactiveExecutor.getVideoPreviewPlayInfo(videoPreviewPlayInfoRequest);
    }

    @Override
    public Mono<ResponseEntity<Resource>> getResource(URL url, MediaType mediaType) {
        return reactiveExecutor.getResource(url, mediaType);
    }

    @Override
    public Mono<ResponseEntity<Resource>> parseVideoUrl(URL url) {
        return reactiveExecutor.parseVideoUrl(url);
    }


}
