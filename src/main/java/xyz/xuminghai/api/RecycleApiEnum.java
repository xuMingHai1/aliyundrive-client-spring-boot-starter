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

package xyz.xuminghai.api;

import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.net.URI;

/**
 * 2021/10/10 14:03 星期日<br/>
 * 阿里云盘回收站操作接口
 *
 * @author xuMingHai
 */
@Getter
public enum RecycleApiEnum {

    /**
     * 移动到回收站
     */
    TRASH(URI.create("https://api.aliyundrive.com/v2/recyclebin/trash"), HttpMethod.POST);


    /**
     * api接口
     */
    private final URI api;

    /**
     * 请求方式
     */
    private final HttpMethod httpMethod;

    RecycleApiEnum(URI api, HttpMethod httpMethod) {
        this.api = api;
        this.httpMethod = httpMethod;
    }

}
