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
 * 2021/11/1 17:26 星期一<br/>
 * 阻塞的执行器接口，继承执行器接口，重写返回值和自己的方法<br/>
 * 改执行器是阻塞的，所以等到的结果是不需要等待
 *
 * @author xuMingHai
 */
public interface BlockExecutor extends Executor {
    /**
     * 根据指定的请求参数，发送list请求
     *
     * @param listRequest list请求参数
     * @return 文件列表的响应
     */
    @Override
    ListResponse list(ListRequest listRequest);

    /**
     * 根据指定的搜索请求参数，发送search请求
     *
     * @param searchRequest 搜索请求参数
     * @return 搜索的响应
     */
    @Override
    SearchResponse search(SearchRequest searchRequest);

    /**
     * 根据文件ID，发送get请求
     *
     * @param fileId 文件ID
     * @return 这个文件的信息
     */
    @Override
    BaseItem get(String fileId);

    /**
     * 根据文件ID，获取文件的下载信息
     *
     * @param fileId 文件ID
     * @return 这个文件的下载信息
     */
    @Override
    DownloadUrlResponse getDownloadUrl(String fileId);


    /**
     * 下载文件
     *
     * @param fileId      文件ID
     * @param path        下载到的目标地址(不需要指定文件名)
     * @param openOptions 参数选项
     * @return 文件字节数
     */
    long downloadFile(String fileId, Path path, OpenOption... openOptions);

    /**
     * 根据创建文件夹的请求参数，发送创建文件夹的请求
     *
     * @param createFolderRequest 创建文件夹的请求参数
     * @return 返回的响应
     */
    @Override
    CreateFolderResponse createFolder(CreateFolderRequest createFolderRequest);

    /**
     * 上传文件
     *
     * @param parentFileId  父目录ID
     * @param path          要上传的文件路径
     * @param checkNameEnum 同名策略
     * @return 上传文件的响应
     */
    @Override
    CreateFileResponse uploadFile(String parentFileId, Path path, CheckNameEnum checkNameEnum);

    /**
     * 根据指定的修改文件名参数，发送修改文件名请求
     *
     * @param updateRequest 修改文件名的请求参数
     * @return 返回的响应
     */
    @Override
    BaseItem update(UpdateRequest updateRequest);

    /**
     * 将文件移入到回收站
     *
     * @param fileId 文件ID
     * @return 返回的响应
     */
    @Override
    Boolean trash(String fileId);
}
