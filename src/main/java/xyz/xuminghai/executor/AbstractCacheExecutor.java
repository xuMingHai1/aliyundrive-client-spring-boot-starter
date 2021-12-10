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

import xyz.xuminghai.cache.Cache;

/**
 * 2021/11/21 18:06 星期日<br/>
 * 缓存执行器的抽象类，定义了方便缓存执行器使用的方法，可能以后会有用处<br/>
 * 为什么缓存中没有加锁？<br/>
 * 1、加锁会影响效率，访问速度变慢，阿里云盘接口的访问有时间限制，一段时间只能支持最多那么多的并发，
 * 搜索文件即便我加上锁，限制同样地请求只能请求一次，不能有效地解决并发量，因为阿里云盘一段时间只能支持那么多
 * 2、缓存主要考虑的是否存在，不需要一致性，即便是多请求几次再加入缓存也可以。加锁之后要考虑锁的细粒度，只能对相同请求加锁，
 * 而这样又要占用很多内存去管理锁。
 * @author xuMingHai
 */
abstract class AbstractCacheExecutor implements Executor{

    /**
     * 缓存实例
     */
    final Cache cache;

    /**
     * 获取缓存超时时间
     */
    static final long GET_CACHE_TIMEOUT = 1000L;

    AbstractCacheExecutor(Cache cache) {
        this.cache = cache;
    }



}
