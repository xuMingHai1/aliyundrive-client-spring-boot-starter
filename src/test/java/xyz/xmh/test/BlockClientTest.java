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

package xyz.xmh.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import xyz.xmh.autoconfigure.CacheAutoConfiguration;
import xyz.xmh.autoconfigure.ClientAutoConfiguration;
import xyz.xmh.pojo.entity.BaseItem;
import xyz.xmh.pojo.response.file.CreateFileResponse;
import xyz.xmh.template.BlockClientTemplate;

import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 2021/10/29 16:24 星期五<br/>
 * 阻塞客户端测试类
 *
 * @author xuMingHai
 */
@SpringBootTest
@ContextConfiguration(classes = {CacheAutoConfiguration.class, ClientAutoConfiguration.class})
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BlockClientTest {

    @Autowired
    private BlockClientTemplate blockClientTemplate;

    /**
     * 测试获取文件列表
     */
    @Test
    @Order(0)
    void testList() {
        // 先休眠5秒等待，令牌刷新完
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(blockClientTemplate.list());
    }

    /**
     * 测试搜索文件
     */
    @Test
    void testSearch() {
        // 循环获取测试缓存
        for (int i = 0; i < 10; i++) {
            System.out.println(blockClientTemplate.search("1"));
        }
    }

    /**
     * 测试下载文件
     */
    @Test
    @Order(1)
    void testDownload() {
        final List<BaseItem> items = blockClientTemplate.list().getItems();
        items.stream().filter(baseItem -> "file".equals(baseItem.getType()))
                .findFirst()
                .ifPresent(baseItem -> blockClientTemplate.downloadFile(baseItem.getFileId(), Paths.get(""), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW));
    }

    /**
     * 测试上传文件
     */
    @Test
    @Order(2)
    void testUpload() {
        try {
            final CreateFileResponse createFileResponse = blockClientTemplate.uploadFile(Paths.get("C:", "Users", "admin", "Desktop", "test.txt"));
            log.info("是否快传：{}", createFileResponse.isRapidUpload());
            log.info("上传文件所在的目录id: {}", createFileResponse.getParentFileId());
        } catch (WebClientResponseException e) {
            System.out.println(e.getResponseBodyAsString());
        }

    }

    /**
     * 测试创建文件夹
     */
    @Test
    void testCreateFolder() {
        System.out.println(blockClientTemplate.createFolder("hello"));
    }


}
