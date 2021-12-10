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

import org.springframework.lang.Nullable;

import java.net.URL;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2021/12/3 8:19 星期五<br/>
 * 处理url的工具类
 * @author xuMingHai
 */
public final class UrlUtils {


    /**
     * 默认的资源过期时间15分钟（阿里云盘资源的默认时间）
     */
    private static final long TIMEOUT = 60 * 15;

    /**
     * 过期时间戳正则表达式
     */
    private static final Pattern EXPIRES_PATTERN = Pattern.compile("x-oss-expires=\\d+");

    private UrlUtils() {}

    /**
     * 获取阿里云盘资源url中的过期时间戳，如果不存在将使用默认的15分钟
     * @param url 资源地址，为null会使用默认时间戳
     * @return 过期时间戳（秒级）减10秒
     */
    public static long getTimeoutStamp(@Nullable URL url) {
        // 过期时间戳
        long timeoutStamp;

        if (url == null) {
            timeoutStamp = Instant.now().getEpochSecond() + TIMEOUT;
        } else {
            final String s = url.toString();
            // 参数索引
            final int i = s.indexOf('?');

            final Matcher matcher = EXPIRES_PATTERN.matcher(s);

            // 拼配过期时间戳，如果i = -1 说明这个URL没有参数
            if (i >= 0 && matcher.find(i)) {
                // 匹配到的应该是 x-oss-expires=时间戳，拆分等号
                timeoutStamp = Long.parseLong(matcher.group().split("=")[1]);
            } else {
                // 如果没有匹配到时间戳，设置为默认的 可能永远不会用到这个
                timeoutStamp = Instant.now().getEpochSecond() + TIMEOUT;
            }
        }

        // 在这个基础上减去10秒，预防获取的时候因为延迟等原因超时
        return timeoutStamp - 10;

    }


}
