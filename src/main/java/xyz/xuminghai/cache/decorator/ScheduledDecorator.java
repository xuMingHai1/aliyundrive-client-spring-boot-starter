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

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 2021/10/21 18:33 星期四<br/>
 * 给定多少秒，定时清空缓存
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
            log.info("【{}】定时{}秒：执行了定时清空缓存", getName(), fixedDelay);
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
    public void put(Object key, Object value, long timestampSeconds) {
        cache.put(key, value, timestampSeconds);
    }

    @Override
    public Object get(Object key) {
        return cache.get(key);
    }

    @Override
    public void remove(Object key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
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
