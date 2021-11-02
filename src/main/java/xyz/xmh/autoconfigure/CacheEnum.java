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

package xyz.xmh.autoconfigure;

/**
 * 2021/10/24 8:09 星期日<br/>
 * 缓存的装饰器
 *
 * @author xuMingHai
 */
public enum CacheEnum {

    /**
     * 日志装饰，用于输出缓存的命中率
     */
    LOGGING,

    /**
     * lru算法，在指定的访问容量到达上限时，优先删除访问量少的缓存
     */
    LRU,

    /**
     * 给定固定延迟，定时删除缓存
     */
    SCHEDULED,

    /**
     * 不添加任何装饰器，如果这个存在则不会给缓存实例添加任何装饰器
     */
    NONE


}
