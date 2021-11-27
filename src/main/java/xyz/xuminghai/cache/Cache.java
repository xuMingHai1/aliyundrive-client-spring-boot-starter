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

package xyz.xuminghai.cache;

import java.util.Set;

/**
 * 2021/10/21 11:00 星期四<br/>
 * 缓存接口，定义缓存功能<br/>
 * 借鉴自mybatis
 *
 * @author xuMingHai
 */
public interface Cache {

    /**
     * 获取缓存名
     *
     * @return 缓存名
     */
    String getName();

    /**
     * 添加缓存内容
     *
     * @param key   请求
     * @param value 响应
     */
    void put(Object key, Object value);

    /**
     * 根据这个请求获取响应内容
     *
     * @param key 请求
     * @return 响应，如果值不存在为null
     */
    Object get(Object key);

    /**
     * 删除指定的缓存
     * @param key 请求
     */
    void remove(Object key);

    /**
     * 清空所有缓存
     */
    void clear();

    /**
     * 获取这个缓存实例的元素个数
     *
     * @return 缓存元素的个数
     */
    long size();

    /**
     * 获取这个缓存实例中所有的key，只读的set集合
     * @return key的set集合，如果没有key则是空的set集合
     */
    Set<Object> keySet();


}
