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

package xyz.xuminghai.pojo.entity.upload;

import lombok.Data;
import xyz.xuminghai.pojo.response.file.CreateFileResponse;

/**
 * 2021/10/30 16:47 星期六<br/>
 * 记录上传的状态信息
 *
 * @author xuMingHai
 */
@Data
public class UploadStatus {

    /**
     * 响应的状态码
     */
    private int responseCode;

    /**
     * 响应的消息
     */
    private String responseMessage;

    /**
     * 如果在失败传回的有错误消息，否则为空字符
     */
    private String errorMessage;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 上传分片
     */
    private CreateFileResponse.PartInfo partInfo;

    /**
     * 要上传文件的起始位置
     */
    private long position;

    public UploadStatus(String fileName, CreateFileResponse.PartInfo partInfo, long position) {
        this.partInfo = partInfo;
        this.position = position;
    }
}
