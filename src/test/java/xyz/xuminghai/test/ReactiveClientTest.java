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

package xyz.xuminghai.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import xyz.xuminghai.pojo.response.file.ListResponse;
import xyz.xuminghai.pojo.response.file.SearchResponse;
import xyz.xuminghai.template.ReactiveClientTemplate;

/**
 * 2021/11/1 23:34 星期一<br/>
 * 反应式客户端测试
 *
 * @author xuMingHai
 */
@SpringBootApplication
@RestController
public class ReactiveClientTest {

    @Autowired
    private ReactiveClientTemplate reactiveClientTemplate;

    public static void main(String[] args) {
        SpringApplication.run(ReactiveClientTest.class, args);
    }

    @RequestMapping("/list")
    Mono<ResponseEntity<ListResponse>> list() {
        return reactiveClientTemplate.list();
    }

    @RequestMapping("/search/{fileName}")
    Mono<ResponseEntity<SearchResponse>> search(@PathVariable String fileName) {
        return reactiveClientTemplate.search(fileName);
    }

    @RequestMapping("/download/{fileId}")
    Mono<ResponseEntity<Resource>> download(@PathVariable String fileId, @RequestHeader HttpHeaders httpHeaders) {
        return reactiveClientTemplate.downloadFile(fileId, httpHeaders);
    }


}
