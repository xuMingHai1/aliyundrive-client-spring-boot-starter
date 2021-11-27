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
 * 2021/11/26 23:18 星期五<br/>
 * 抽象的数据访问层父类
 * @author xuMingHai
 */
public abstract class AbstractReactiveDao implements ReactiveDao{

    /**
     * 阿里云盘访问
     */
    protected final WebClient webClient;

    public AbstractReactiveDao(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public WebClient.ResponseSpec sendRequest(HttpMethod httpMethod, URI uri) {
        return webClient.method(httpMethod).uri(uri)
                .retrieve();
    }

    @Override
    public WebClient.ResponseSpec sendRequest(HttpMethod httpMethod, URI uri, Object body) {
        return webClient.method(httpMethod).uri(uri)
                .bodyValue(body)
                .retrieve();
    }


}
