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

package xyz.xuminghai.pojo.entity.video;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.net.URL;

/**
 * 2021/11/27 0:20 星期六<br/>
 * 实时转码任务
 * @author xuMingHai
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LiveTranscodingTask {

    /**
     * 模板ID
     * <pre>
     * 480p 576p 被称为度SD(Standard Definition)（标清）
     *
     * 720p 被称为HD(High Definition)（高清）
     *
     * 1080p 被称为FHD(Full High Definition)（全高清）
     *
     * 4k 被称为UHD(Ultra High Definition)(超高答清)(或 4k UHD)
     *
     * 8k 被称为FUHD(Full Ultra High Definition)(8k超高清)(或 8k UHD)
     * </pre>
     */
    private String templateId;

    /**
     * 模板名
     */
    private String templateName;

    /**
     * 状态
     */
    private String status;

    /**
     * 阶段
     */
    private String stage;

    /**
     * 地址
     */
    private URL url;
}
