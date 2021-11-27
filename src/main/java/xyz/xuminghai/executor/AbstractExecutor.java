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

package xyz.xuminghai.executor;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import xyz.xuminghai.core.ReactiveFileDao;
import xyz.xuminghai.core.ReactiveRecycleDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 2021/11/26 20:16 星期五<br/>
 * 抽象地执行器
 * @author xuMingHai
 */
abstract class AbstractExecutor implements Executor{

    final ReactiveFileDao reactiveFileDao;

    final ReactiveRecycleDao reactiveRecycleDao;

    /**
     * 匹配m3u8文件中的ts文件
     */
    private static final String M3U8_REGES = "^media-\\d*.ts\\?\\S*";

    /**
     * 换行分隔符
     */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    AbstractExecutor(ReactiveFileDao reactiveFileDao, ReactiveRecycleDao reactiveRecycleDao) {
        this.reactiveFileDao = reactiveFileDao;
        this.reactiveRecycleDao = reactiveRecycleDao;
    }

    Mono<ResponseEntity<Resource>> getResource(URL url, MediaType mediaType) {
        return Mono.create(monoSink -> {
            try {
                reactiveFileDao.sendRequest(HttpMethod.GET, url.toURI())
                        .toEntity(Resource.class)
                        .subscribe(entity -> monoSink.success(ResponseEntity.status(entity.getStatusCode())
                                .headers(entity.getHeaders())
                                .contentType(mediaType)
                                .body(entity.getBody())), monoSink::error);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 发送url获取m3u8文件，将ts相对路径改为绝对路径
     * @param url m3u8文件地址
     * @return 修改后的m3u8文件
     */
    Mono<ResponseEntity<Resource>> parseM3u8(URL url) {
        final String prefixUrl = url.toString().split("media.m3u8")[0];
        final StringBuilder sb = new StringBuilder();
        return Mono.create(monoSink -> {
            try {
                reactiveFileDao.sendRequest(HttpMethod.GET, url.toURI())
                        .toEntity(Resource.class)
                        .subscribe(entity -> {
                            try (InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(entity.getBody()).getInputStream());
                                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                                bufferedReader.lines().forEach(s -> {
                                    if (s.matches(M3U8_REGES)) {
                                        sb.append(prefixUrl);
                                    }
                                    sb.append(s).append(LINE_SEPARATOR);
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            monoSink.success(ResponseEntity.status(entity.getStatusCode())
                                    .headers(entity.getHeaders())
                                    .contentLength(sb.length())
                                    .body(new ByteArrayResource(sb.toString().getBytes(StandardCharsets.UTF_8), "装载m3u8文件")));
                        }, monoSink::error);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });

    }





}
