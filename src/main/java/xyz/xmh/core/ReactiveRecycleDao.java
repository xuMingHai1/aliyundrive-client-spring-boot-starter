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

package xyz.xmh.core;

import org.springframework.web.reactive.function.client.WebClient;

/**
 * 2021/10/26 23:40 星期二<br/>
 * 反应式回收站接口
 *
 * @author xuMingHai
 */
public interface ReactiveRecycleDao {

    /**
     * 将文件移入回收站
     *
     * @param fileId 文件ID
     * @return 后续响应操作
     */
    WebClient.ResponseSpec trash(String fileId);


}
