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

package xyz.xuminghai.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 2021/10/20 4:22 星期三<br/>
 * 简化调用，api程序员
 *
 * @author xuMingHai
 */
public final class Md5Utils {

    private Md5Utils() {
    }


    /**
     * 进行md5摘要
     *
     * @param bytes 要进行摘要的消息
     * @return md5值
     */
    public static String digest(byte[] bytes) {
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            return new BigInteger(1, md5.digest(bytes)).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


}
