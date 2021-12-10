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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import xyz.xuminghai.io.AliyunInputResource;
import xyz.xuminghai.pojo.entity.video.LiveTranscodingTask;
import xyz.xuminghai.pojo.response.file.ListResponse;
import xyz.xuminghai.pojo.response.file.SearchResponse;
import xyz.xuminghai.template.ReactiveClientTemplate;

import java.util.List;
import java.util.Objects;

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
    Mono<ResponseEntity<AliyunInputResource>> download(@PathVariable String fileId) {
        return reactiveClientTemplate.downloadFile(fileId);
    }

    /**
     * 播放视频，可以将这个地址添加到播放器
     * @param fileId 文件ID
     * @return M3u8文件
     */
    @RequestMapping("/playVideo/{fileId}")
    Mono<ResponseEntity<AliyunInputResource>> playVideo(@PathVariable String fileId) {
        return reactiveClientTemplate.getVideoPreviewPlayInfo(fileId)
                .flatMap(entity -> {
                    final List<LiveTranscodingTask> list = Objects.requireNonNull(entity.getBody()).getVideoPreviewPlayInfo().getLiveTranscodingTaskList();
                    return reactiveClientTemplate.getVideo(list.get(list.size() - 1).getUrl());
                });
    }

    /**
     * 获取视频缩略图
     * @param fileId 文件ID
     * @return 视频缩略图
     */
    @RequestMapping("/getVideoThumbnail/{fileId}")
    Mono<ResponseEntity<AliyunInputResource>> getVideoThumbnail(@PathVariable String fileId) {
        return reactiveClientTemplate.getVideoThumbnail(fileId);
    }

    /**
     * 获取图片资源
     * @param fileId 文件ID
     * @return 图片资源
     */
    @RequestMapping("/getImage/{fileId}")
    Mono<ResponseEntity<AliyunInputResource>> getImage(@PathVariable String fileId) {
        return reactiveClientTemplate.getImage(fileId);
    }


    /**
     * 获取文档资源
     * @param fileId 文件ID
     * @return 文档资源
     */
    @RequestMapping("/getDoc/{fileId}")
    Mono<ResponseEntity<AliyunInputResource>> getDoc(@PathVariable String fileId) {
        return reactiveClientTemplate.getDoc(fileId);
    }

    /**
     * 播放音频，可以添加到播放器中
     * @param fileId 文件ID
     * @return 音频资源
     */
    @RequestMapping("/playAudio/{fileId}")
    Mono<ResponseEntity<AliyunInputResource>> playAudio(@PathVariable String fileId) {
        return reactiveClientTemplate.getAudioPlayInfo(fileId).flatMap(entity -> reactiveClientTemplate.getAudio(Objects.requireNonNull(entity.getBody())
                .getTemplateList()
                .get(0)
                .getUrl()));
    }

}
