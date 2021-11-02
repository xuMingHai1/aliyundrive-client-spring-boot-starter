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

package xyz.xmh.core.impl;

import org.springframework.web.reactive.function.client.WebClient;
import xyz.xmh.api.RecycleEnum;
import xyz.xmh.core.ReactiveRecycleDao;
import xyz.xmh.pojo.request.FileIdRequest;

/**
 * 2021/10/26 23:44 星期二<br/>
 * 响应式回收站api访问类
 *
 * @author xuMingHai
 */
public class ReactiveRecycleDaoImpl implements ReactiveRecycleDao {

    private final WebClient webClient;

    public ReactiveRecycleDaoImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public WebClient.ResponseSpec trash(String fileId) {
        final FileIdRequest fileIdRequest = new FileIdRequest(fileId);
        final RecycleEnum trash = RecycleEnum.TRASH;
        return webClient.method(trash.getHttpMethod()).uri(trash.getApi())
                .bodyValue(fileIdRequest)
                .retrieve();
    }
}
