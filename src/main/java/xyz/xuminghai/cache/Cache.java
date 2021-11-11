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

/**
 * 2021/10/21 11:00 星期四<br/>
 * 缓存接口，自定义缓存策略请实现这个接口和继承缓存实例类<br/>
 * 比如：使用Redis作为缓存<br/>
 * 必须要创建一个带有缓存名称的构造器，例如：<br/>
 * <pre>
 *  public MyCache(final String name) {
 *       if (name == null) {
 *         throw new IllegalArgumentException("Cache instances require an cacheName");
 *       }
 *       this.name = name;
 *        initialize();
 *     }
 * </pre>
 * <p>
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
     *
     * @param key 请求
     * @return 之前请求的内容，如果不存在则为null
     */
    Object remove(Object key);

    /**
     * 清除这个缓存实例
     */
    void clear();

    /**
     * 获取这个缓存实例的元素个数
     *
     * @return 缓存元素的个数
     */
    int size();


}
