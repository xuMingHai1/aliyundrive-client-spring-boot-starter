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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import xyz.xuminghai.autoconfigure.CacheAutoConfiguration;
import xyz.xuminghai.autoconfigure.ClientAutoConfiguration;
import xyz.xuminghai.pojo.entity.BaseItem;
import xyz.xuminghai.pojo.response.file.CreateFileResponse;
import xyz.xuminghai.pojo.response.file.UploadFolderResponse;
import xyz.xuminghai.template.BlockClientTemplate;

import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
     * 错误的测试，用于查看错误响应信息
     */
    @Test
    @Order(1)
    void test() {
        System.out.println(blockClientTemplate.list("1324"));
    }

    /**
     * 测试搜索文件
     */
    @Test
    void testSearch() {
        System.out.println(blockClientTemplate.search("1"));
        System.out.println(blockClientTemplate.search("2"));
        System.out.println(blockClientTemplate.search("3"));
        System.out.println(blockClientTemplate.search("1"));
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
        final CreateFileResponse createFileResponse = blockClientTemplate.uploadFile(Paths.get("README.md"));
        log.info("是否快传：{}", createFileResponse.isRapidUpload());
        log.info("上传文件所在的目录id: {}", createFileResponse.getParentFileId());

    }

    /**
     * 测试多线程上传目录
     */
    @Test
    @Order(3)
    void testUploadFolder() {
        final ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                final UploadFolderResponse uploadFolderResponse = blockClientTemplate.uploadFolder(Paths.get(""));
                System.out.println("--------------------------------文件夹上传响应-------------------------------------");
                System.out.println(uploadFolderResponse);
                System.out.println("--------------------------------文件夹上传响应的文件夹集合-------------------------------------");
                System.out.println(uploadFolderResponse.getFolder().getFolderList());
                System.out.println("--------------------------------文件夹上传响应的文件集合-------------------------------------");
                System.out.println(uploadFolderResponse.getFolder().getFileList());
            });
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(3, TimeUnit.MINUTES)) {
                log.info("没有在规定时间上传完成");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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
