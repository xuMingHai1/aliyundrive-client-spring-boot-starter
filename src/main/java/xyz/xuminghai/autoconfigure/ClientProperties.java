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

/**
 * 2021/10/23 7:07 星期六<br/>
 *
 * @author xuMingHai
 */
@Data
@ConfigurationProperties(prefix = "xyz.xuminghai.client")
public class ClientProperties {

    /**
     * 阿里云盘的刷新令牌，这个是必须添加的
     */
    private String refreshToken;

    /**
     * 访问令牌刷新时间间隔，默认是7000，单位秒
     */
    private int fixedDelay = 7000;

    /**
     * 是否在 DEBUG 级别记录表单数据，在 TRACE 级别记录标题。 两者都可能包含敏感信息。<br/>
     * 默认设置为true
     */
    private boolean enableLoggingRequestDetails = true;

    /**
     * 配置在需要聚合输入流时可以缓冲的字节数的限制。 <br/>
     * 这可能是解码为单个DataBuffer 、 ByteBuffer 、 byte[] 、 Resource 、 String等的结果。<br/>
     * 在分割输入流时也可能发生这种情况，例如分隔文本，在这种情况下，限制适用于在分隔符之间缓冲的数据。<br/>
     * 如果这个缓冲区过小，会出现异常，你需要调大缓存区
     * 默认情况下，为 2M，单位字节，-1 表示无限制
     */
    private int maxInMemorySize = 2 << 20;

    /**
     * 上传重试次数，阿里云盘的上传有时候会出现409，多重试几次<br/>
     * <pre>如果重试次数为0或负数，在失败时不会重新上传   默认次数：3</pre>
     */
    private int uploadRetries = 3;

    /**
     * 上传文件时分片的大小，文件小于分片的大小就不会分片上传<br/>
     * <pre>默认值：10MB       单位：MB</pre>
     */
    private int uploadFragmentation = 10;


}
