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

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 2021/10/21 12:39 星期四<br/>
 * 默认缓存实现，可以被别的装饰器增强功能
 *
 * @author xuMingHai
 */
public class BaseCache extends AbstractCacheInstance {

    /**
     * 存放缓存内容的map
     */
    private final Map<Object, Object> cache = new HashMap<>();

    /**
     * 存放带有过期时间key的map
     */
    private final Map<Object, Long> expirationMap = new HashMap<>();

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void put(Object key, Object value, long timestampSeconds) {
        cache.put(key, value);
        expirationMap.put(key, timestampSeconds);
    }

    @Override
    public Object get(Object key) {
        final Long timestampSeconds = expirationMap.get(key);
        // 如果这个key存在过期时间
        if (timestampSeconds != null) {
            // 判断这个key是否已经过期，如果已经过期删除缓存，返回null
            if (Instant.now().getEpochSecond() > timestampSeconds) {
                expirationMap.remove(key);
                cache.remove(key);
                return null;
            }
        }
        return cache.get(key);
    }

    @Override
    public void remove(Object key) {
        cache.remove(key);
        expirationMap.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
        expirationMap.clear();
    }

    @Override
    public long size() {
        return cache.size();
    }

    @Override
    protected Supplier<Set<Object>> getKeySet() {
        return cache::keySet;
    }
}
