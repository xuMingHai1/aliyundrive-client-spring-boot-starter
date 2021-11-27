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

package xyz.xuminghai.pojo.request.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import xyz.xuminghai.autoconfigure.TokenStatic;

/**
 * 2021/11/25 20:32 星期四<br/>
 * 视频预览播放信息请求
 * @author xuMingHai
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoPreviewPlayInfoRequest {

    /**
     * 类别
     */
    private String category = "live_transcoding";

    /**
     * 阿里云盘ID
     */
    private String driveId = TokenStatic.DEFAULT_DRIVE_ID;

    /**
     * 文件ID
     */
    private String fileId;

    /**
     * 模板ID
     */
    private String templateId = "";

    /**
     * 创建视频预览播放信息请求
     * @param fileId 文件ID
     */
    public VideoPreviewPlayInfoRequest(String fileId) {
        this.fileId = fileId;
    }
}
