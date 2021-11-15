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

import java.text.DecimalFormat;

/**
 * 2021/10/21 15:48 星期四<br/>
 * 日志装饰，用于记录缓存命中率
 *
 * @author xuMingHai
 */
@Slf4j
public class LoggingDecorator implements Cache {

    /**
     * 被装饰的对象
     */
    private final Cache cache;
    /**
     * 十进制格式化，保留小数点后三位
     */
    private final DecimalFormat decimalFormat = new DecimalFormat("0.000");
    /**
     * 调用次数
     */
    private int count;
    /**
     * 命中
     */
    private int hits;

    public LoggingDecorator(Cache cache) {
        this.cache = cache;
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
        // 调用次数累计
        count++;
        final Object value = cache.get(key);
        // 如果从缓存中找到，命中次数加1
        if (value != null) {
            hits++;
        }
        log.info("【{}】缓存命中率：{}", getName(), getHitRatio());
        return value;
    }

    @Override
    public void remove(Object key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        // 清除缓存，重新统计
        count = 0;
        hits = 0;
        cache.clear();
    }

    @Override
    public long size() {
        return cache.size();
    }

    /**
     * 获取命中率，命中除以调用次数
     *
     * @return 命中率
     */
    private String getHitRatio() {
        return decimalFormat.format((double) hits / count);
    }

}
