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

package xyz.xmh.util;


import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 2021/10/18 22:03 星期一<br/>
 * 我也要造轮子，主要是不想引入别的依赖，而现有的又不能满足我的需求
 *
 * @author xuMingHai
 */
@Slf4j
public final class IoUtils {

    private IoUtils() {
    }


    /**
     * 根据指定的字符集，将输入流中的数据转为字符串（调用者关闭输入流）
     *
     * @param inputStream 输入流
     * @param charset     字符集
     * @return 转换后的字符串
     */
    public static String toString(InputStream inputStream, Charset charset) {
        if (inputStream == null) {
            return "";
        }

        StringWriter stringWriter = new StringWriter();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        bufferedReader.lines().forEach(stringWriter::write);
        stringWriter.flush();
        return stringWriter.toString();
    }

    /**
     * 打印指定的字符集转换输入流中的数据，完毕后将数据推回输入流（调用者关闭输入流）
     *
     * @param inputStream 输入流
     * @param charset     字符集
     * @return 数据推回后的输入流
     */
    public static InputStream printlnPushback(InputStream inputStream, Charset charset) {

        final String string = toString(inputStream, charset);
        final byte[] bytes = string.getBytes(charset);
        final PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream, bytes.length);
        log.info(string);
        try {
            pushbackInputStream.unread(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pushbackInputStream;
    }

    /**
     * 打印Java 虚拟机的默认字符集转换输入流中的数据，完毕后将数据推回输入流（调用者关闭输入流）
     *
     * @param inputStream 输入流
     * @return 数据推回后的输入流
     */
    public static InputStream printlnPushback(InputStream inputStream) {
        return printlnPushback(inputStream, Charset.defaultCharset());
    }

    /**
     * 使用 Java 虚拟机的默认字符集，将输入流中的数据转为字符串（调用者关闭输入流）
     *
     * @param inputStream 输入流
     * @return 转换后的字符串
     */
    public static String toString(InputStream inputStream) {
        return toString(inputStream, Charset.defaultCharset());
    }

    /**
     * 将输入流中的数据使用指定的字符集转为行的字符集合（调用者关闭输入流）
     *
     * @param inputStream 输入流
     * @param charset     指定的字符集
     * @return list集合
     */
    public static List<String> lines(InputStream inputStream, Charset charset) {
        if (inputStream == null) {
            return Collections.emptyList();
        }
        List<String> list = new ArrayList<>();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        bufferedReader.lines().forEach(list::add);
        return list;
    }

    /**
     * 使用 Java 虚拟机的默认字符集，将输入流中的数据转为字符串集合（调用者关闭输入流）
     *
     * @param inputStream 输入流
     * @return list集合
     */
    public static List<String> lines(InputStream inputStream) {
        return lines(inputStream, Charset.defaultCharset());
    }


}
