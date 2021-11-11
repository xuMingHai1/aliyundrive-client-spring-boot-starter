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

package xyz.xuminghai.pojo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * token响应实体
 *
 * @author xuMingHai
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TokenResponse {

    private String defaultSboxDriveId;
    private String role;
    private String deviceId;
    private String userName;
    private Boolean needLink;
    private LocalDateTime expireTime;
    private Boolean pinSetup;
    private Boolean needRpVerify;
    private String avatar;
    /**
     * token类型
     */
    private String tokenType;

    /**
     * token令牌
     */
    private String accessToken;
    private String defaultDriveId;
    private String domainId;
    private String refreshToken;
    private Boolean isFirstLogin;
    private String userId;
    private String nickName;
    private List<String> existLink;
    private String state;
    private Integer expiresIn;
    private String status;

}
