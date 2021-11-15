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

/**
 * 2021/11/15 16:45 星期一<br/>
 * 序列化器接口，定义了序列化的功能
 * @author xuMingHai
 */
public interface Serializer {

    byte[] EMPTY_ARRAY = new byte[0];

    /**
     * 将对象序列化为字节数组
     * @param o 要进行序列化的对象
     * @return 序列化的结果
     */
    byte[] serialize(Object o);

    /**
     * 将字节数组反序列化为对象
     * @param bytes 要进行反序列化的数组
     * @return 反序列化的对象
     */
    Object deserialize(byte[] bytes);

}
