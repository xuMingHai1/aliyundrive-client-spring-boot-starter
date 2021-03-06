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

package xyz.xuminghai.executor;

import xyz.xuminghai.core.ReactiveFileDao;
import xyz.xuminghai.core.ReactiveRecycleDao;

/**
 * 2021/11/26 20:16 星期五<br/>
 * 抽象地执行器
 * @author xuMingHai
 */
abstract class AbstractExecutor implements Executor{

    final ReactiveFileDao reactiveFileDao;

    final ReactiveRecycleDao reactiveRecycleDao;

    AbstractExecutor(ReactiveFileDao reactiveFileDao, ReactiveRecycleDao reactiveRecycleDao) {
        this.reactiveFileDao = reactiveFileDao;
        this.reactiveRecycleDao = reactiveRecycleDao;
    }





}
