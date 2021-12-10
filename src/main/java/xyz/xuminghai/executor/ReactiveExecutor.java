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
import xyz.xuminghai.io.AliyunInputResource;
import xyz.xuminghai.pojo.entity.BaseItem;
import xyz.xuminghai.pojo.enums.CheckNameEnum;
import xyz.xuminghai.pojo.request.file.*;
import xyz.xuminghai.pojo.response.file.*;

import java.net.URL;
import java.nio.file.Path;

/**
 * 2021/10/25 16:41 星期一<br/>
 * 反应性执行器接口，重写执行器的返回值，和自己的方法
 *
 * @author xuMingHai
 */
public interface ReactiveExecutor extends Executor {

    /**
     * 根据指定的请求参数，发送list请求
     *
     * @param listRequest list请求参数
     * @return Http异步文件列表的响应
     */
    @Override
    Mono<ResponseEntity<ListResponse>> list(ListRequest listRequest);

    /**
     * 根据指定的搜索请求参数，发送search请求
     *
     * @param searchRequest 搜索请求参数
     * @return Http异步搜索的响应
     */
    @Override
    Mono<ResponseEntity<SearchResponse>> search(SearchRequest searchRequest);

    /**
     * 根据文件ID，发送get请求
     *
     * @param fileId 文件ID
     * @return Http异步这个文件的信息
     */
    @Override
    Mono<ResponseEntity<BaseItem>> get(String fileId);

    /**
     * 根据文件ID，获取文件的下载信息
     *
     * @param fileId 文件ID
     * @return Http异步这个文件的下载信息
     */
    @Override
    Mono<ResponseEntity<DownloadUrlResponse>> getDownloadUrl(String fileId);

    /**
     * 下载文件
     * @param fileId      文件ID
     * @return 文件资源
     */
    Mono<ResponseEntity<AliyunInputResource>> downloadFile(String fileId);

    /**
     * 根据创建文件夹的请求参数，发送创建文件夹的请求
     *
     * @param createFolderRequest 创建文件夹的请求参数
     * @return Http异步返回的响应
     */
    @Override
    Mono<ResponseEntity<CreateFolderResponse>> createFolder(CreateFolderRequest createFolderRequest);

    /**
     * 上传文件
     *
     * @param parentFileId  父目录ID
     * @param path          要上传的文件路径
     * @param checkNameEnum 同名策略
     * @return 异步上传文件的响应
     */
    @Override
    Mono<CreateFileResponse> uploadFile(String parentFileId, Path path, CheckNameEnum checkNameEnum);

    /**
     * 根据指定的修改文件名参数，发送修改文件名请求
     *
     * @param updateRequest 修改文件名的请求参数
     * @return Http异步返回的响应
     */
    @Override
    Mono<ResponseEntity<BaseItem>> update(UpdateRequest updateRequest);

    /**
     * 将文件或文件夹移入回收站
     *
     * @param fileId 文件ID
     * @return 没有http实体的异步响应
     */
    @Override
    Mono<ResponseEntity<Void>> trash(String fileId);

    /**
     * 获取视频播放的预览信息
     * @param videoPreviewPlayInfoRequest 请求体
     * @return 返回的响应
     */
    @Override
    Mono<ResponseEntity<VideoPreviewPlayInfoResponse>> getVideoPreviewPlayInfo(VideoPreviewPlayInfoRequest videoPreviewPlayInfoRequest);

    /**
     * 获取视频资源
     * @param url 视频资源路径
     * @return 视频资源m3u8
     */
    Mono<ResponseEntity<AliyunInputResource>> getVideo(URL url);

    /**
     * 获取url的资源
     * @param url 资源地址
     * @return 资源访问类
     */
    Mono<ResponseEntity<AliyunInputResource>> getResource(URL url);

    /**
     * 获取播放音频信息
     * @param fileId 文件ID
     * @return 响应后的操作
     */
    @Override
    Mono<ResponseEntity<AudioPlayInfoResponse>> getAudioPlayInfo(String fileId);

}
