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

package xyz.xuminghai.test;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import xyz.xuminghai.cache.AbstractCacheInstance;
import xyz.xuminghai.serializer.Serializer;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 2021/11/12 2:51 星期五<br/>
 * 简单的redis缓存实现
 * @author xuMingHai
 */
@Component
public class RedisCache extends AbstractCacheInstance {

    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisCache(RedisTemplate<Object, Object> redisTemplate, Serializer serializer) {
        // 设置value的序列化器
        redisTemplate.setValueSerializer(new RedisSerializer<Object>() {
            @Override
            public byte[] serialize(Object o) throws SerializationException {
                return serializer.serialize(o);
            }

            @Override
            public Object deserialize(byte[] bytes) throws SerializationException {
                return serializer.deserialize(bytes);
            }
        });
        // 设置key的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 使用池化的Lettuce连接工厂
        final LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(new RedisStandaloneConfiguration(), LettucePoolingClientConfiguration.defaultConfiguration());
        lettuceConnectionFactory.afterPropertiesSet();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        this.redisTemplate = redisTemplate;
    }


    /**
     * 添加缓存内容
     *
     * @param key   请求
     * @param value 响应
     */
    @Override
    public void put(Object key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void put(Object key, Object value, long timestampSeconds) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expireAt(key, Instant.ofEpochSecond(timestampSeconds));
    }

    /**
     * 根据这个请求获取响应内容
     *
     * @param key 请求
     * @return 响应，如果值不存在为null
     */
    @Override
    public Object get(Object key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除指定的缓存
     * @param key 请求
     */
    @Override
    public void remove(Object key) {
        redisTemplate.delete(key);
    }

    /**
     * 清空所有缓存
     */
    @Override
    public void clear() {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.flushDb();
            return null;
        });
    }

    /**
     * 获取这个缓存实例的元素个数
     *
     * @return 缓存元素的个数
     */
    @Override
    public long size() {
        return Optional.ofNullable(redisTemplate.execute(RedisConnection::dbSize)).orElse(0L);
    }

    @Override
    protected Supplier<Set<Object>> getKeySet() {
        return () -> redisTemplate.keys("*");
    }
}
