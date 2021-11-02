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

package xyz.xmh.core;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import xyz.xmh.pojo.enums.CheckNameEnum;
import xyz.xmh.pojo.request.file.*;
import xyz.xmh.pojo.response.file.CreateFileResponse;

import java.nio.file.Path;


/**
 * 反应式文件接口
 *
 * @author xuMingHai
 */
public interface ReactiveFileDao {

    /**
     * 自定义请求参数的文件列表获取
     *
     * @param listRequest 文件请求参数
     * @return 响应后的操作
     */
    WebClient.ResponseSpec list(ListRequest listRequest);

    /**
     * 根据指定的文件名和文件类型搜索文件
     *
     * @param searchRequest 自定义搜索请求参数
     * @return 响应后的操作
     */
    WebClient.ResponseSpec search(SearchRequest searchRequest);

    /**
     * 获取指定文件的信息
     *
     * @param fileId 文件ID
     * @return 响应操作
     */
    WebClient.ResponseSpec get(String fileId);

    /**
     * 获取文件的下载地址信息
     *
     * @param fileId 文件ID
     * @return 响应操作
     */
    WebClient.ResponseSpec getDownloadUrl(String fileId);

    /**
     * 下载文件，支持分段下载，断点续传
     *
     * @param fileId      文件ID
     * @param httpHeaders 表示 HTTP 请求头
     * @return 响应实体
     */
    Mono<ResponseEntity<Resource>> downloadFile(String fileId, HttpHeaders httpHeaders);

    /**
     * 获取多个文件的下载url
     *
     * @param multiDownloadUrlRequest 多文件下载请求
     * @return url
     */
    @Deprecated
    WebClient.ResponseSpec getMultiDownloadUrl(MultiDownloadUrlRequest multiDownloadUrlRequest);

    /**
     * 下载多个文件，支持分段下载，断点续传
     *
     * @param multiDownloadUrlRequest 多个文件请求
     * @param httpHeaders             http请求头
     * @return 响应实体
     */
    @Deprecated
    Mono<ResponseEntity<Resource>> multiDownloadFile(MultiDownloadUrlRequest multiDownloadUrlRequest, HttpHeaders httpHeaders);

    /**
     * 创建文件夹
     *
     * @param createFolderRequest 请求参数
     * @return 响应后的操作
     */
    WebClient.ResponseSpec createFolder(CreateFolderRequest createFolderRequest);

    /**
     * 上传文件（支持快传和分片上传）
     *
     * @param parentFileId  父目录ID
     * @param path          文件路径
     * @param checkNameEnum 同名策略
     * @return 响应后的操作
     */
    Mono<CreateFileResponse> uploadFile(String parentFileId, Path path, CheckNameEnum checkNameEnum);

    /**
     * 修改文件名
     *
     * @param updateRequest 修改文件名的请求参数
     * @return 响应后续操作
     */
    WebClient.ResponseSpec update(UpdateRequest updateRequest);

}

