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
 * 2021/12/9 21:39 星期四<br/>
 * get方法请求参数
 * @author xuMingHai
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetRequest {

    private String driveId = TokenStatic.DEFAULT_DRIVE_ID;
    /**
     * 文件ID
     */
    private String fileId;

    /**
     * 图片缩略图处理
     */
    private String imageThumbnailProcess = "image/resize,w_400/format,jpeg";

    /**
     * 图片显示url处理
     */
    private String imageUrlProcess = "image/resize,w_1920/format,jpeg";

    /**
     * 视频缩略图过程
     */
    private String videoThumbnailProcess = "video/snapshot,t_0,f_jpg,ar_auto,w_300";

    /**
     * url过期时间
     */
    private int urlExpireSec = 1600;

    public GetRequest(String fileId) {
        this.fileId = fileId;
    }
}
