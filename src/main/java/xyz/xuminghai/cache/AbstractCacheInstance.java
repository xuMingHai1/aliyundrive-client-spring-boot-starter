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

import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 2021/10/26 21:33 星期二<br/>
 * 抽象的缓存实例模板类，自定义缓存实例需要继承这个模板类
 *
 * @author xuMingHai
 */
public abstract class AbstractCacheInstance implements Cache {

    private final String name;

    public AbstractCacheInstance() {
        this.name = this.getClass().getSimpleName();
    }

    /**
     * 获取缓存名
     * @return 缓存名
     */
    @Override
    public final String getName() {
        return this.name;
    }

    /**
     * 提供缓存实例keySet映射
     * @return keySet提供者，可以为null
     */
    protected abstract Supplier<Set<Object>> getKeySet();

    @Override
    public final Set<Object> keySet() {
        if (getKeySet() == null || getKeySet().get() == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(getKeySet().get());
    }
}
