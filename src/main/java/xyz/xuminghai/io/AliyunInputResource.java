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

import io.micrometer.core.instrument.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.AbstractResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import xyz.xuminghai.exception.AlyunResourceExpiredException;
import xyz.xuminghai.util.UrlUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * 2021/11/30 4:01 星期二<br/>
 * 阿里云盘资源访问类，对应阿里云盘资源url的资源类
 * @author xuMingHai
 */
@Slf4j
public class AliyunInputResource extends AbstractResource {

    /**
     * 阿里云盘资源的url
     */
    private final URL url;

    /**
     * 资源的过期时间戳
     */
    private final long timeoutStampSeconds;

    /**
     * http状态码
     */
    private final HttpStatus httpStatus;

    /**
     * http响应头信息
     */
    private final HttpHeaders httpHeaders;


    /**
     * 反序列化时使用
     */
    protected AliyunInputResource() {
        this.url = null;
        this.timeoutStampSeconds = 0;
        this.httpStatus = null;
        this.httpHeaders = null;
    }

    /**
     * 阿里云盘资源访问类
     * @param url 资源路径
     * @throws IOException 获取连接时出现的异常
     */
    public AliyunInputResource(URL url) throws IOException {
        this.url = Objects.requireNonNull(url, "阿里云盘资源的url不能为null");
        this.timeoutStampSeconds = UrlUtils.getTimeoutStamp(url);
        final HttpURLConnection connection = getConnection();
        final LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>(connection.getHeaderFields());
        // 关闭这个连接
        connection.disconnect();
        // noinspection ConstantConditions
        this.httpStatus = HttpStatus.valueOf(Integer.parseInt(multiValueMap.remove(null).get(0).split(" ")[1]));
        this.httpHeaders = new HttpHeaders(multiValueMap);

    }

    @NonNull
    @Override
    public URL getURL() {
        return this.url;
    }

    /**
     * 获取资源的过期时间戳
     * @return 过期时间戳（秒）
     */
    public long getTimeoutStampSeconds() {
        return this.timeoutStampSeconds;
    }

    /**
     * 获取这个资源的响应头信息
     * @return httpheaders
     */
    public HttpHeaders getHttpHeaders() {
        return this.httpHeaders;
    }

    /**
     * 获取这个资源的状态码
     * @return httpStatus
     */
    public HttpStatus getHttpStatus() {
       return this.httpStatus;
    }

    /**
     * 获取响应实体
     * @return ResponseEntity<AliyunInputResource>
     */
    public final ResponseEntity<AliyunInputResource> getResponseEntity() {
        return new ResponseEntity<>(this, getHttpHeaders(), getHttpStatus());
    }

    /**
     * 获取阿里云盘的资源
     * @return 输入流
     * @throws IOException 表示发生了某种 I/O 异常。 此类是由失败或中断的 I/O 操作产生的异常的一般类。
     */
    @NonNull
    @Override
    public InputStream getInputStream() throws IOException {
        return getConnection().getInputStream();
    }

    /**
     * 获取资源的长度，来自响应头contentLength
     * @return 资源长度，当内容长度未知时返回 -1
     */
    @Override
    public long contentLength() {
        return getHttpHeaders().getContentLength();
    }

    @NonNull
    @Override
    public String getDescription() {
        return "阿里云盘资源访问类";
    }

    /**
     * 检测资源是否存在
     * @return 存在返回true
     */
    @Override
    public boolean exists() {
        return contentLength() > 0;
    }

    @Override
    public long lastModified() {
        return getHttpHeaders().getLastModified();
    }

    /**
     * 获取这个url资源的文件名
     * @return 文件名，如果响应不存在文件名则返回null
     */
    @Nullable
    @Override
    public String getFilename() {
        return getHttpHeaders().getContentDisposition().getFilename();
    }

    /**
     * 获取一个阿里云盘资源已经打开的http连接
     * @return http
     * @throws IOException 连接时发生io异常
     */
    public HttpURLConnection getConnection() throws IOException {
        // 判断资源是否过期
        if (Instant.now().getEpochSecond() >= getTimeoutStampSeconds()) {
            throw new AlyunResourceExpiredException("这个资源已经过期无法访问");
        }

        final HttpURLConnection connection = (HttpURLConnection) getURL().openConnection();
        // 设置接收的类型
        connection.setRequestProperty(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
        connection.setRequestProperty(HttpHeaders.REFERER, "https://www.aliyundrive.com/");
        connection.setRequestProperty(HttpHeaders.USER_AGENT, "aliyundrive-client-spring-boot-starter.AliyunInputResource");
        // 设置长连接
        connection.setRequestProperty(HttpHeaders.CONNECTION, "keep-alive");
        // 设置获取资源的范围
        connection.setRequestProperty(HttpHeaders.RANGE, "bytes=0-");
        // 设置不缓存
        connection.setRequestProperty(HttpHeaders.CACHE_CONTROL, "no-cache");
        connection.setRequestProperty(HttpHeaders.PRAGMA, "no-cache");
        // 设置读取超时为1秒
        connection.setReadTimeout(1000);
        // 获取连接
        connection.connect();
        // 判断状态码是否是错误的
        if (HttpStatus.valueOf(connection.getResponseCode()).isError()) {
            log.error("连接阿里云盘资源时发生错误：{}", errorLog(connection));
        }
        return connection;
    }

    /**
     * 发生错误时，获取连接的错误信息，关闭http连接
     * @param connection http连接
     * @return 错误信息
     * @throws IOException 发送io异常
     */
    private String errorLog(HttpURLConnection connection) throws IOException {
        final String requestMethod = connection.getRequestMethod();
        final StringBuilder sb = new StringBuilder("\n请求方法：" + requestMethod);
        final int responseCode = connection.getResponseCode();
        if (responseCode != -1) {
            sb.append("\n状态代码：").append(responseCode);
        }
        final String responseMessage = connection.getResponseMessage();
        Optional.ofNullable(responseMessage)
                .ifPresent(s -> sb.append("\n响应消息：").append(s));
        try (InputStream errorStream = connection.getErrorStream()) {
            Optional.ofNullable(errorStream)
                    .ifPresent(inputStream -> sb.append("\n错误信息：").append(IOUtils.toString(inputStream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 关闭http连接
        connection.disconnect();
        return sb.toString();
    }


}
