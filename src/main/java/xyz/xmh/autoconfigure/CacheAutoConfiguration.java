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

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import xyz.xmh.cache.Cache;
import xyz.xmh.cache.NullCache;
import xyz.xmh.cache.decorator.LoggingDecorator;
import xyz.xmh.cache.decorator.LruDecorator;
import xyz.xmh.cache.decorator.ScheduledDecorator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 2021/10/24 7:29 星期日<br/>
 * 缓存自动配置类
 *
 * @author xuMingHai
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(WebClient.class)
@EnableConfigurationProperties(CacheProperties.class)
public class CacheAutoConfiguration {

    @Bean
    public Cache cache(CacheProperties cacheProperties) {
        if (!cacheProperties.isEnableCache()) {
            return new NullCache();
        }
        Cache cache = null;
        try {
            // 创建缓存实例
            final Class<? extends Cache> cacheClass = cacheProperties.getCacheInstance().asSubclass(Cache.class);
            final Constructor<? extends Cache> constructor = cacheClass.getConstructor(String.class);
            cache = constructor.newInstance(cacheClass.getSimpleName());

            // 增强缓存的装饰器
            final List<CacheEnum> decoratorList = cacheProperties.getDecorator();
            // 如果存在none，不添加任何装饰器
            if (decoratorList.contains(CacheEnum.NONE)) {
                return cache;
            }
            // 给缓存实例添加装饰器
            for (CacheEnum cacheEnum : decoratorList) {
                // 日志装饰器
                if (cacheEnum == CacheEnum.LOGGING) {
                    cache = new LoggingDecorator(cache);
                }

                // lru装饰器
                if (cacheEnum == CacheEnum.LRU) {
                    cache = new LruDecorator(cache, cacheProperties.getLruMaxCapacity());
                }

                // scheduled装饰器
                if (cacheEnum == CacheEnum.SCHEDULED) {
                    cache = new ScheduledDecorator(cache, cacheProperties.getScheduledFixedDelay());
                }
            }

        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return cache;
    }

}
