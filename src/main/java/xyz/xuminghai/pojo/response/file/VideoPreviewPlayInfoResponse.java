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

package xyz.xuminghai.pojo.response.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import xyz.xuminghai.pojo.entity.video.VideoPreviewPlayInfo;

/**
 * 2021/11/25 23:40 星期四<br/>
 * 播放视频预览信息响应体
 * @author xuMingHai
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoPreviewPlayInfoResponse {

    /**
     * 域ID
     */
    private String domainId;

    /**
     * 阿里云ID
     */
    private String driveId;

    /**
     * 文件ID
     */
    private String fileId;

    /**
     * 视频预览信息
     */
    private VideoPreviewPlayInfo videoPreviewPlayInfo;



}
