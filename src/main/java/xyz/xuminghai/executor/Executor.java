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

import xyz.xuminghai.pojo.enums.CheckNameEnum;
import xyz.xuminghai.pojo.request.file.*;

import java.nio.file.Path;

/**
 * 2021/10/26 14:28 星期二<br/>
 * 客户端执行器，这个执行器定义了客户端共有的功能
 *
 * @author xuMingHai
 */
public interface Executor {

    /**
     * 根据指定的请求参数，发送list请求
     *
     * @param listRequest list请求参数
     * @return 文件列表的响应
     */
    Object list(ListRequest listRequest);

    /**
     * 根据指定的搜索请求参数，发送search请求
     *
     * @param searchRequest 搜索请求参数
     * @return 搜索的响应
     */
    Object search(SearchRequest searchRequest);

    /**
     * 根据文件ID，发送get请求
     *
     * @param fileId 文件ID
     * @return 这个文件的信息
     */
    Object get(String fileId);

    /**
     * 根据文件ID，获取文件的下载信息
     *
     * @param fileId 文件ID
     * @return 这个文件的下载信息
     */
    Object getDownloadUrl(String fileId);

    /**
     * 根据创建文件夹的请求参数，发送创建文件夹的请求
     *
     * @param createFolderRequest 创建文件夹的请求参数
     * @return 返回的响应
     */
    Object createFolder(CreateFolderRequest createFolderRequest);

    /**
     * 上传文件
     *
     * @param parentFileId  父目录ID
     * @param path          要上传的文件路径
     * @param checkNameEnum 同名策略
     * @return 上传文件的响应
     */
    Object uploadFile(String parentFileId, Path path, CheckNameEnum checkNameEnum);

    /**
     * 根据指定的修改文件名参数，发送修改文件名请求
     *
     * @param updateRequest 修改文件名的请求参数
     * @return 返回的响应
     */
    Object update(UpdateRequest updateRequest);

    /**
     * 将文件或文件夹移入回收站
     *
     * @param fileId 文件ID
     * @return 返回的响应
     */
    Object trash(String fileId);

    /**
     * 获取视频播放的预览信息
     * @param videoPreviewPlayInfoRequest 请求体
     * @return 返回的响应
     */
    Object getVideoPreviewPlayInfo(VideoPreviewPlayInfoRequest videoPreviewPlayInfoRequest);

    /**
     * 获取播放音频信息
     * @param fileId 文件ID
     * @return 响应后的操作
     */
    Object getAudioPlayInfo(String fileId);

}
