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

package xyz.xmh.cache.decorator;

import lombok.extern.slf4j.Slf4j;
import xyz.xmh.cache.Cache;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 2021/10/21 18:33 星期四<br/>
 * 给定多少秒，定时清除缓存
 *
 * @author xuMingHai
 */
@Slf4j
public class ScheduledDecorator implements Cache {

    /**
     * 被装饰的对象
     */
    private final Cache cache;

    @SuppressWarnings("AlibabaThreadPoolCreation")
    public ScheduledDecorator(Cache cache, int fixedDelay) {
        this.cache = cache;
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            log.info("【定时清除缓存】_{}：执行了清除缓存", getName());
            clear();
        }, fixedDelay, fixedDelay, TimeUnit.SECONDS);
    }

    @Override
    public String getName() {
        return cache.getName();
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object get(Object key) {
        return cache.get(key);
    }

    @Override
    public Object remove(Object key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public int size() {
        return cache.size();
    }
}
