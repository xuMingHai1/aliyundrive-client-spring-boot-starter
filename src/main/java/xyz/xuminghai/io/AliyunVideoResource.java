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

package xyz.xuminghai.io;

import org.springframework.lang.NonNull;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 2021/12/8 17:24 星期三<br/>
 * 阿里云盘视频资源
 * @author xuMingHai
 */
public class AliyunVideoResource extends AliyunInputResource {

    /**
     * 匹配media.m3u8，不情愿的量词
     */
    private static final String M3U8_REGEX = "(.+media\\.m3u8.+)??";

    /**
     * 匹配m3u8文件中的ts文件
     */
    private static final String TS_REGEX = "(^media-\\d+\\.ts\\?.+)??";

    /**
     * 换行分隔符
     */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * m3u8文件资源
     */
    private final byte[] bytes;

    /**
     * 反序列化使用
     */
    protected AliyunVideoResource() {
        super();
        this.bytes = null;
    }

    /**
     * 阿里云盘视频资源访问类
     *
     * @param url 视频资源路径
     * @throws IOException 获取连接时出现的异常
     */
    public AliyunVideoResource(URL url) throws IOException {
        super(url);
        // 检查这个url是否是视频资源
        if (!url.toString().matches(M3U8_REGEX)) {
            throw new IllegalArgumentException("这个url不是视频资源：" + url);
        }
        this.bytes = parseM3u8(super.getInputStream());
        // 修改资源长度
        getHttpHeaders().setContentLength(this.bytes.length);
    }

    /**
     * 获取m3u8视频文件资源
     * @return 输入流
     */
    @NonNull
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.bytes);
    }

    @NonNull
    @Override
    public String getDescription() {
        return "阿里云盘视频资源类";
    }

    /**
     * 将m3u8文件的ts文件改成绝对路径
     * @param inputStream m3u8文件
     * @return 字节数组
     */
    private byte[] parseM3u8(InputStream inputStream) {
        // ts文件前置url
        final String prefixUrl = getURL().toString().split("media.m3u8")[0];
        final StringBuilder sb = new StringBuilder();

        try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))){
            bufferedReader.lines().forEach(s -> {
                // 匹配到ts文件，加上前置url，将m3u8的地址改成绝对路径
                if (s.matches(TS_REGEX)) {
                    sb.append(prefixUrl);
                }
                sb.append(s).append(LINE_SEPARATOR);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

}
