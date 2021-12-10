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

package xyz.xuminghai.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import xyz.xuminghai.io.AliyunInputResource;
import xyz.xuminghai.io.AliyunVideoResource;

import java.util.ArrayList;

/**
 * 2021/11/15 16:37 星期一<br/>
 * 使用kryo的序列化器
 * @author xuMingHai
 */
public class KryoSerializer implements Serializer{

    private static final Class<?> READ_ONLY_HTTP_HEADERS_CLASS;

    private static final Class<?> HEADERS_UTILS_$_1_CLASS;

    private static final Class<?> UNMODIFIABLE_RANDOM_ACCESS_LIST;

    private static final int MAX_CAPACITY = Runtime.getRuntime().availableProcessors() * 2;

    static {
        try {
            READ_ONLY_HTTP_HEADERS_CLASS = Class.forName("org.springframework.http.ReadOnlyHttpHeaders");
            HEADERS_UTILS_$_1_CLASS = Class.forName("io.netty.handler.codec.HeadersUtils$1");
            UNMODIFIABLE_RANDOM_ACCESS_LIST = Class.forName("java.util.Collections$UnmodifiableRandomAccessList");
        } catch (ClassNotFoundException e) {
           throw new Error(e);
        }
    }

    private static final Pool<Kryo> KRYO_POOL = new Pool<Kryo>(true, false, MAX_CAPACITY) {

        @Override
        protected Kryo create() {
            final Kryo kryo = new Kryo();
            kryo.setRegistrationRequired(false);

            // 注册要序列化的类
            kryo.register(AliyunInputResource.class, 9);
            kryo.register(AliyunVideoResource.class, 10);

            // 自定义创建对象实例
            kryo.register(ResponseEntity.class)
                    .setInstantiator(() -> new ResponseEntity<>(HttpStatus.OK));

            /*
                这里可能会有一些问题
                之前的类型是ReadOnlyHttpHeaders，是不允许修改的
                为了反序列化变为了可以修改，这样可能会有一些问题
                不过问题应该不大
             */
            kryo.register(READ_ONLY_HTTP_HEADERS_CLASS)
                    .setInstantiator(HttpHeaders::new);

            kryo.register(HEADERS_UTILS_$_1_CLASS)
                    .setInstantiator(ArrayList::new);

            kryo.register(UNMODIFIABLE_RANDOM_ACCESS_LIST)
                    .setInstantiator(ArrayList::new);

            return kryo;
        }
    };

    private static final Pool<Input> INPUT_POOL = new Pool<Input>(true, false, MAX_CAPACITY) {
        @Override
        protected Input create() {
            return new Input();
        }
    };

    private static final Pool<Output> OUTPUT_POOL = new Pool<Output>(true, false, MAX_CAPACITY) {

        @Override
        protected Output create() {
            return new Output(1024, -1);
        }
    };


    /**
     * 将对象序列化为字节数组
     *
     * @param o 要进行序列化的对象
     * @return 序列化的结果
     */
    @Override
    public byte[] serialize(Object o) {
        if (o == null) {
            return EMPTY_ARRAY;
        }

        final Output output = OUTPUT_POOL.obtain();
        KRYO_POOL.obtain().writeClassAndObject(output, o);
        final byte[] bytes = output.toBytes();
        output.close();
        return bytes;
    }

    /**
     * 将字节数组反序列化为对象
     *
     * @param bytes 要进行反序列化的数组
     * @return 反序列化的对象
     */
    @Override
    public Object deserialize(byte[] bytes) {
        if (ObjectUtils.isEmpty(bytes)) {
            return null;
        }

        final Input input = INPUT_POOL.obtain();
        input.setBuffer(bytes);
        final Kryo kryo = KRYO_POOL.obtain();
        final Object o = kryo.readClassAndObject(input);
        input.close();
        return o;
    }
}
