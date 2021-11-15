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
    private final Map<Object, Integer> lruMap;

    public LruDecorator(Cache cache, int maxCapacity) {
        this.cache = cache;
        /*
          最大容量
         */
        this.lruMap = new LinkedHashMap<Object, Integer>(16, .75F, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Object, Integer> eldest) {
                if (size() > maxCapacity) {
                    final Object key = eldest.getKey();
                    cache.remove(key);
                    final Integer count = remove(key);
                    log.info("访问量为【{}】的缓存被删除，如果访问量过大，请考虑扩大访问集合的容量", count);
                }
                return false;
            }
        };
    }

    @Override
    public String getName() {
        return cache.getName();
    }

    @Override
    public void put(Object key, Object value) {
        lruMap.put(key, 0);
        cache.put(key, value);
    }

    @Override
    public Object get(Object key) {
        Integer integer = lruMap.get(key);
        if (integer == null) {
            integer = 0;
        }
        lruMap.put(key, integer + 1);
        return cache.get(key);
    }

    @Override
    public void remove(Object key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        lruMap.clear();
        cache.clear();
    }

    @Override
    public long size() {
        return cache.size();
    }
}
