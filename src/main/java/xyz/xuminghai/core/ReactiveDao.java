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

package xyz.xuminghai.core;

import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

/**
 * 2021/11/27 18:44 星期六<br/>
 * 数据访问层的顶级接口
 * @author xuMingHai
 */
public interface ReactiveDao {

    /**
     * 向阿里云盘发动指定请求，得到响应处理
     * @param httpMethod http请求方式
     * @param uri 请求地址
     * @return 响应后的操作
     */
    WebClient.ResponseSpec sendRequest(HttpMethod httpMethod, URI uri);

    /**
     * 向阿里云盘发动指定请求，得到响应处理
     * @param httpMethod http请求方式
     * @param uri 请求地址
     * @param body 请求参数
     * @return 响应后的操作
     */
    WebClient.ResponseSpec sendRequest(HttpMethod httpMethod, URI uri, Object body);


}
