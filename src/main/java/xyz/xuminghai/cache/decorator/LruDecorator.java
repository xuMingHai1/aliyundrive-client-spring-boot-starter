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

package xyz.xuminghai.cache.decorator;

import lombok.extern.slf4j.Slf4j;
import xyz.xuminghai.cache.Cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;

/**
 * 2021/10/21 16:47 星期四<br/>
 * LRU缓存，在容量不够的时候，最先清除访问量少的缓存
 *
 * @author xuMingHai
 */
@Slf4j
public class LruDecorator implements Cache {

    /**
     * 被装饰的对象
     */
    private final Cache cache;
    /**
     * 计算访问量的集合，容量到达最大容量时，先删除访问量最少的，集合中key存放的是请求参数，value是访问次数
     */
    private final Map<Object, LongAdder> lruMap;

    public LruDecorator(Cache cache, int maxCapacity) {
        this.cache = cache;
        this.lruMap = new LinkedHashMap<Object, LongAdder>(maxCapacity, .75F, true) {

            @Override
            protected boolean removeEldestEntry(Map.Entry<Object, LongAdder> eldest) {
                final Object key = eldest.getKey();
                // 如果缓存内元素的数量大于设定的缓存数量就删除访问量较少的key
                if (size() > maxCapacity) {
                    cache.remove(key);
                    final LongAdder count = remove(key);
                    log.info("key为【{}】访问量为【{}】的缓存被删除，如果访问量过大，请考虑扩大缓存元素的最大容量", key, count);
                }
                return false;
            }

            @Override
            public synchronized LongAdder put(Object key, LongAdder value) {
                // 对于访问可以支持并发，添加不支持，确保size()的准确性
                return super.put(key, value);
            }
        };

        // 在初始化时如果缓存实例中存在key，添加到lruMap中
        cache.keySet().forEach(key -> lruMap.put(key, new LongAdder()));

    }

    @Override
    public String getName() {
        return cache.getName();
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
        lruMap.put(key, new LongAdder());
    }

    @Override
    public void put(Object key, Object value, long timestampSeconds) {
        cache.put(key, value, timestampSeconds);
        lruMap.put(key, new LongAdder());
    }

    @Override
    public Object get(Object key) {
        final Object value = cache.get(key);
        if (value != null) {
            // 查询时对访问量进行原子自增
            lruMap.get(key).increment();
        } else {
            // 如果key不存在，可能是已经过期，尝试删除LruMap中可能存在的key
            lruMap.remove(key);
        }
        return value;
    }

    @Override
    public void remove(Object key) {
        cache.remove(key);
        lruMap.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
        lruMap.clear();
    }

    @Override
    public long size() {
        return cache.size();
    }

    @Override
    public Set<Object> keySet() {
        return cache.keySet();
    }
}
