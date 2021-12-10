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

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import xyz.xuminghai.cache.Cache;
import xyz.xuminghai.io.AliyunInputResource;
import xyz.xuminghai.pojo.entity.BaseItem;
import xyz.xuminghai.pojo.enums.CheckNameEnum;
import xyz.xuminghai.pojo.request.file.*;
import xyz.xuminghai.pojo.response.file.*;
import xyz.xuminghai.util.UrlUtils;

import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;

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

    @Override
    public Mono<ResponseEntity<ListResponse>> list(ListRequest listRequest) {
        final String key = "reactive_list_" + listRequest.hashCode();
        return getAndPutDefaultTimeout(key, () -> reactiveExecutor.list(listRequest));
    }

    @Override
    public Mono<ResponseEntity<SearchResponse>> search(SearchRequest searchRequest) {
        final String key = "reactive_search_" + searchRequest.hashCode();
        return getAndPutDefaultTimeout(key, () -> reactiveExecutor.search(searchRequest));
    }


    @Override
    public Mono<ResponseEntity<BaseItem>> get(String fileId) {
        final String key = "reactive_get_" + fileId;
        return getAndPutDefaultTimeout(key, () -> reactiveExecutor.get(fileId));
    }

    @Override
    public Mono<ResponseEntity<DownloadUrlResponse>> getDownloadUrl(String fileId) {
        final String key = "reactive_getDownloadUrl_" + fileId;
        return get(key, () -> reactiveExecutor.getDownloadUrl(fileId)
                .doOnNext(t -> cache.put(key, t, UrlUtils.getTimeoutStamp(Objects.requireNonNull(t.getBody()).getUrl()))));
    }

    @Override
    public Mono<ResponseEntity<AliyunInputResource>> downloadFile(String fileId) {
        final String key = "reactive_downloadFile_" + fileId;
        return get(key, () -> reactiveExecutor.downloadFile(fileId)
                .doOnNext(t -> cache.put(key, t, Objects.requireNonNull(t.getBody()).getTimeoutStampSeconds())));
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
        final String key = "reactive_getVideoPreviewPlayInfo_" + videoPreviewPlayInfoRequest.getFileId();
        return get(key, () -> reactiveExecutor.getVideoPreviewPlayInfo(videoPreviewPlayInfoRequest)
                .doOnNext(t -> cache.put(key, t, UrlUtils.getTimeoutStamp(Objects.requireNonNull(t.getBody())
                        .getVideoPreviewPlayInfo()
                        .getLiveTranscodingTaskList()
                        .get(0)
                        .getUrl()))));
    }

    @Override
    public Mono<ResponseEntity<AliyunInputResource>> getResource(URL url) {
        final String key = "reactive_getResource_" + url;
        return get(key, () -> reactiveExecutor.getResource(url)
                .doOnNext(t -> cache.put(key, t, Objects.requireNonNull(t.getBody()).getTimeoutStampSeconds())));
    }

    @Override
    public Mono<ResponseEntity<AliyunInputResource>> getVideo(URL url) {
        final String key = "reactive_getVideo_" + url;
        return get(key, () -> reactiveExecutor.getVideo(url)
                .doOnNext(t -> cache.put(key, t, Objects.requireNonNull(t.getBody()).getTimeoutStampSeconds())));
    }


    @Override
    public Mono<ResponseEntity<AudioPlayInfoResponse>> getAudioPlayInfo(String fileId) {
        final String key = "reactive_getAudioPlayInfo_" + fileId;
        return get(key, () -> reactiveExecutor.getAudioPlayInfo(fileId)
                .doOnNext(t -> cache.put(key, t, UrlUtils.getTimeoutStamp(Objects.requireNonNull(t.getBody())
                        .getTemplateList()
                        .get(0)
                        .getUrl()))));
    }

    /**
     * 获取缓存并返回，如果没有则从供应商添加缓存设置默认的缓存超时时间并返回
     * @param key 缓存的key
     * @param supplier 提供缓存元素
     * @param <T> 缓存元素类型
     * @return 返回的响应
     */
    private <T> Mono<T> getAndPutDefaultTimeout(String key, Supplier<Mono<T>> supplier) {
        return get(key, () -> supplier.get()
                .doOnNext(t -> cache.put(key, t, UrlUtils.getTimeoutStamp(null))));
    }

    /**
     * 获取缓存并返回，如果没有则从供应商添加缓存并返回
     * @param key 缓存的key
     * @param supplier 提供缓存元素
     * @param <T> 缓存元素类型
     * @return 返回的响应
     */
    private <T> Mono<T> getAndPut(String key, Supplier<Mono<T>> supplier) {
        return get(key, () -> supplier.get()
                .doOnNext(t -> cache.put(key, t)));
    }

    /**
     * 获取缓存并返回
     * @param key 缓存的key
     * @param supplier 提供缓存元素
     * @param <T> 缓存元素类型
     * @return 返回的响应
     */
    @SuppressWarnings("unchecked")
    private <T> Mono<T> get(String key, Supplier<Mono<T>> supplier) {
        // 从缓存中获取元素
        return Mono.justOrEmpty((T) cache.get(key))
                // 获取超时时间
                .timeout(Duration.ofMillis(GET_CACHE_TIMEOUT))
                // 如果获取的元素为null，则返回供应商
                .switchIfEmpty(supplier.get());
    }

}
