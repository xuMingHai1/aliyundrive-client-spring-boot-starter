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
import org.springframework.http.HttpMethod;

import java.net.URL;
import java.time.LocalDateTime;

/**
 * 2021/10/9 21:31 星期六<br/>
 * 下载地址响应
 *
 * @author xuMingHai
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DownloadUrlResponse {

    /**
     * 请求方式
     */
    private HttpMethod method;

    /**
     * 地址
     */
    private URL url;

    /**
     * 内部地址
     */
    private URL internalUrl;
    private URL cdnUrl;

    /**
     * 到期时间
     */
    private LocalDateTime expiration;

    private String contentHash;

    private String contentHashName;

    private String crc64Hash;

    /**
     * 大小
     */
    private long size;
    private Ratelimit ratelimit;

    /**
     * 速率限制，-1表示没有限制
     */
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Ratelimit {
        /**
         * 速度
         */
        private int partSpeed;

        /**
         * 大小
         */
        private int partSize;
    }

}
