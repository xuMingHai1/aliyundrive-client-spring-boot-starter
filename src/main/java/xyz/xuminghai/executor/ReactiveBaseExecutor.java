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
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import xyz.xuminghai.core.ReactiveFileDao;
import xyz.xuminghai.core.ReactiveRecycleDao;
import xyz.xuminghai.pojo.entity.BaseItem;
import xyz.xuminghai.pojo.enums.CheckNameEnum;
import xyz.xuminghai.pojo.request.file.CreateFolderRequest;
import xyz.xuminghai.pojo.request.file.ListRequest;
import xyz.xuminghai.pojo.request.file.SearchRequest;
import xyz.xuminghai.pojo.request.file.UpdateRequest;
import xyz.xuminghai.pojo.response.file.*;

import java.nio.file.Path;

/**
 * 2021/10/25 17:06 星期一<br/>
 * 反应性执行器的基本实现
 *
 * @author xuMingHai
 */
public class ReactiveBaseExecutor implements ReactiveExecutor {

    private final ReactiveFileDao reactiveFileDao;

    private final ReactiveRecycleDao reactiveRecycleDao;

    public ReactiveBaseExecutor(ReactiveFileDao reactiveFileDao, ReactiveRecycleDao reactiveRecycleDao) {
        this.reactiveFileDao = reactiveFileDao;
        this.reactiveRecycleDao = reactiveRecycleDao;
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
    public Mono<ResponseEntity<Resource>> downloadFile(String fileId, HttpHeaders httpHeaders) {
        return reactiveFileDao.downloadFile(fileId, httpHeaders);
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

}
