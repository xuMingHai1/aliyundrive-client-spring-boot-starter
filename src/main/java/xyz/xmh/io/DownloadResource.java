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

package xyz.xmh.io;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.util.ObjectUtils;
import xyz.xmh.pojo.response.file.DownloadUrlResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2021/10/15 21:39 星期五<br/>
 * 增强原生的资源，使用从阿里云盘获取到的httpHeaders补充信息，方面转换为{@link org.springframework.core.io.support.ResourceRegion}
 *
 * @author xuMingHai
 */
public class DownloadResource extends InputStreamResource {

    /**
     * url连接
     */
    private final URLConnection urlConnection;
    /**
     * 响应头信息
     */
    private final HttpHeaders responseHttpHeaders;

    /**
     * 资源下载信息
     */
    private final DownloadUrlResponse downloadUrlResponse;

    private DownloadResource(InputStream inputStream, URLConnection urlConnection, DownloadUrlResponse downloadUrlResponse) {
        super(inputStream, "阿里云盘文件下载");
        this.urlConnection = urlConnection;
        this.downloadUrlResponse = downloadUrlResponse;
        final Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
        final Map<String, List<String>> hashMap = new HashMap<>(headerFields.size());
        hashMap.putAll(headerFields);
        hashMap.put("HttpStatus", hashMap.remove(null));

        final MultiValueMapAdapter<String, String> multiValueMapAdapter = new MultiValueMapAdapter<>(hashMap);
        this.responseHttpHeaders = new HttpHeaders(multiValueMapAdapter);
    }

    /**
     * 创建下载资源
     *
     * @param downloadUrlResponse 下载信息
     * @param requestHttpHeaders  请求头信息
     * @return 阿里云盘下载资源
     * @throws IOException 创建输入流时发生I/O错误
     */
    public static DownloadResource createResource(DownloadUrlResponse downloadUrlResponse, HttpHeaders requestHttpHeaders) throws IOException {
        final URLConnection urlConnection = downloadUrlResponse.getUrl().openConnection();
        urlConnection.setRequestProperty(HttpHeaders.REFERER, "https://www.aliyundrive.com/");

        // 设置范围下载
        if (!requestHttpHeaders.getRange().isEmpty()) {
            urlConnection.setRequestProperty(HttpHeaders.RANGE, HttpRange.toString(requestHttpHeaders.getRange()));
            // 设置资源为变更，中断重下
            final List<String> list = requestHttpHeaders.get(HttpHeaders.IF_RANGE);
            if (!ObjectUtils.isEmpty(list)) {
                urlConnection.setRequestProperty(HttpHeaders.IF_RANGE, list.toString());
            }
        }
        return new DownloadResource(urlConnection.getInputStream(), urlConnection, downloadUrlResponse);
    }

    /**
     * 获取内容总长度
     *
     * @return 内容字节数
     */
    @Override
    public long contentLength() {
        return downloadUrlResponse.getSize();
    }

    /**
     * 获取响应头信息
     *
     * @return 响应头信息
     */
    public HttpHeaders getResponseHttpHeaders() {
        return responseHttpHeaders;
    }

    @Override
    public URL getURL() {
        return urlConnection.getURL();
    }

    /**
     * 获取http状态码
     *
     * @return http状态码
     */
    public HttpStatus getHttpStatus() {
        final List<String> list = responseHttpHeaders.get("HttpStatus");
        if (!ObjectUtils.isEmpty(list)) {
            return HttpStatus.resolve(Integer.parseInt(list.get(0).split(" ")[1]));
        }
        return HttpStatus.OK;
    }

}
