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

package xyz.xuminghai.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.xuminghai.cache.AbstractCacheInstance;
import xyz.xuminghai.cache.BaseCache;
import xyz.xuminghai.cache.decorator.CacheDecoratorEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 2021/10/24 7:28 星期日<br/>
 * 缓存配置类
 *
 * @author xuMingHai
 */
@ConfigurationProperties("xyz.xuminghai.cache")
@Data
public class CacheProperties {

    /**
     * 是否启用缓存
     */
    private boolean enableCache = true;

    /**
     * 增强缓存功能的装饰，默认开启：logging，lru，scheduled
     */
    private List<CacheDecoratorEnum> decorator = new ArrayList<CacheDecoratorEnum>(3) {{
        add(CacheDecoratorEnum.LOGGING);
        add(CacheDecoratorEnum.LRU);
        add(CacheDecoratorEnum.SCHEDULED);
    }};

    /**
     * lru装饰缓存元素的最大容量，到达这个容量上限时，删除访问量最少的缓存，
     * 默认1024
     */
    private int lruMaxCapacity = 1024;

    /**
     * scheduled装饰的固定延迟后清空缓存，默认3600，单位：秒
     */
    private int scheduledFixedDelay = 3600;

    /**
     * 自定义缓存实例，继承{@link AbstractCacheInstance}，比如使用Redis作为缓存<br/>
     * 默认使用jvm缓存
     */
    private Class<? extends AbstractCacheInstance> cacheInstance = BaseCache.class;

}
