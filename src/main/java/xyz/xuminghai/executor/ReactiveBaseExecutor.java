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
import xyz.xuminghai.core.ReactiveFileDao;
import xyz.xuminghai.core.ReactiveRecycleDao;
import xyz.xuminghai.io.AliyunInputResource;
import xyz.xuminghai.io.AliyunVideoResource;
import xyz.xuminghai.pojo.entity.BaseItem;
import xyz.xuminghai.pojo.enums.CheckNameEnum;
import xyz.xuminghai.pojo.request.file.*;
import xyz.xuminghai.pojo.response.file.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

/**
 * 2021/10/25 17:06 星期一<br/>
 * 反应性执行器的基本实现
 *
 * @author xuMingHai
 */
public class ReactiveBaseExecutor extends AbstractExecutor implements ReactiveExecutor {

    public ReactiveBaseExecutor(ReactiveFileDao reactiveFileDao, ReactiveRecycleDao reactiveRecycleDao) {
        super(reactiveFileDao, reactiveRecycleDao);
    }

    @Override
    public Mono<ResponseEntity<ListResponse>> list(ListRequest listRequest) {
        return reactiveFileDao.list(listRequest).toEntity(ListResponse.class);
    }

    @Override
    public Mono<ResponseEntity<SearchResponse>> search(SearchRequest searchRequest) {
        return reactiveFileDao.search(searchRequest).toEntity(SearchResponse.class);
    }

    @Override
    public Mono<ResponseEntity<BaseItem>> get(String fileId) {
        return reactiveFileDao.get(fileId).toEntity(BaseItem.class);
    }

    @Override
    public Mono<ResponseEntity<DownloadUrlResponse>> getDownloadUrl(String fileId) {
        return reactiveFileDao.getDownloadUrl(fileId).toEntity(DownloadUrlResponse.class);
    }

    @Override
    public Mono<ResponseEntity<CreateFolderResponse>> createFolder(CreateFolderRequest createFolderRequest) {
        return reactiveFileDao.createFolder(createFolderRequest).toEntity(CreateFolderResponse.class);
    }

    @Override
    public Mono<ResponseEntity<AliyunInputResource>> downloadFile(String fileId) {
        return getDownloadUrl(fileId).flatMap(entity -> getResource((Objects.requireNonNull(entity.getBody()).getUrl())));
    }

    @Override
    public Mono<CreateFileResponse> uploadFile(String parentFileId, Path path, CheckNameEnum checkNameEnum) {
        return reactiveFileDao.uploadFile(parentFileId, path, checkNameEnum);
    }

    @Override
    public Mono<ResponseEntity<Void>> trash(String fileId) {
        return reactiveRecycleDao.trash(fileId).toBodilessEntity();
    }

    @Override
    public Mono<ResponseEntity<BaseItem>> update(UpdateRequest updateRequest) {
        return reactiveFileDao.update(updateRequest)
                .toEntity(BaseItem.class);
    }

    @Override
    public Mono<ResponseEntity<VideoPreviewPlayInfoResponse>> getVideoPreviewPlayInfo(VideoPreviewPlayInfoRequest videoPreviewPlayInfoRequest) {
        return reactiveFileDao.getVideoPreviewPlayInfo(videoPreviewPlayInfoRequest)
                .toEntity(VideoPreviewPlayInfoResponse.class);
    }

    @Override
    public Mono<ResponseEntity<AliyunInputResource>> getVideo(URL url) {
        try {
            return Mono.just(new AliyunVideoResource(url).getResponseEntity());
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

    @Override
    public Mono<ResponseEntity<AliyunInputResource>> getResource(URL url) {
        try {
            return Mono.just(new AliyunInputResource(url).getResponseEntity());
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

    @Override
    public Mono<ResponseEntity<AudioPlayInfoResponse>> getAudioPlayInfo(String fileId) {
        return reactiveFileDao.getAudioPlayInfo(fileId)
                .toEntity(AudioPlayInfoResponse.class);
    }
}
