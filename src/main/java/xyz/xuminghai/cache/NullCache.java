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

import java.util.Set;

/**
 * 2021/10/26 22:53 星期二<br/>
 * 一个空对象缓存，表示没有启用缓存
 *
 * @author xuMingHai
 */
public class NullCache implements Cache {

    public static final String NULL_CACHE = "NullCache";

    @Override
    public String getName() {
        return NULL_CACHE;
    }

    @Override
    public void put(Object key, Object value) {
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public void remove(Object key) {
    }

    @Override
    public void clear() {
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public Set<Object> keySet() {
        return null;
    }
}
