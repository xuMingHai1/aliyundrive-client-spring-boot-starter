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
import org.junit.jupiter.api.Test;
import xyz.xuminghai.io.AliyunInputResource;
import xyz.xuminghai.pojo.response.file.UploadFolderResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/**
 * 2021/10/29 19:33 星期五<br/>
 * 其他的测试
 *
 * @author xuMingHai
 */
@Slf4j
public class OtherTest {

    /**
     * 填充文件
     */
    @org.junit.jupiter.api.Test
    void test() {
        // 默认是10MB 单位是MB, 可以修改前面的10
        final byte[] bytes = new byte[10 << 20];

        Arrays.fill(bytes, (byte) 'u');

        try (ReadableByteChannel channel = Channels.newChannel(new ByteArrayInputStream(bytes));
             FileChannel fileChannel = FileChannel.open(Paths.get("", "uploadTest.txt"), StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            fileChannel.transferFrom(channel, fileChannel.size(), bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 测试上传文件夹响应
     */
    @org.junit.jupiter.api.Test
    void testUploadFolderResponse() {
        final UploadFolderResponse uploadFolderResponse = new UploadFolderResponse();

        // 顶级目录 root目录
        final UploadFolderResponse.Folder rootFolder = new UploadFolderResponse.Folder();
        rootFolder.setFileId("root");
        rootFolder.setFileName("root");
        uploadFolderResponse.setFolder(rootFolder);

        // 顶级目录下存在的文件
        final UploadFolderResponse.File aFile = new UploadFolderResponse.File();
        aFile.setRapidUpload(true);
        aFile.setParentFileId(rootFolder.getFileId());
        aFile.setFileName("aFile");
        aFile.setFileId(rootFolder.getFileId() + "_" + aFile.getFileName());
        aFile.setParentFolder(rootFolder);
        rootFolder.addFile(aFile);

        // 顶级目录下存在的目录 test目录
        final UploadFolderResponse.Folder testFolder = new UploadFolderResponse.Folder();
        testFolder.setParentFileId(rootFolder.getFileId());
        testFolder.setFileName("test");
        testFolder.setFileId(rootFolder.getFileId() + "-" + testFolder.getFileName());
        testFolder.setParentFolder(rootFolder);
        rootFolder.addFolder(testFolder);

        // test目录下的文件
        final UploadFolderResponse.File bFile = new UploadFolderResponse.File();
        bFile.setParentFileId(testFolder.getFileId());
        bFile.setFileName("bFile");
        bFile.setFileId(testFolder.getFileId() + "_" + bFile.getFileName());
        bFile.setParentFolder(testFolder);
        testFolder.addFile(bFile);


        System.out.println(uploadFolderResponse);
    }

    @Test
    void test1() throws IOException {
        final URL url = new URL("");
        final AliyunInputResource resource = new AliyunInputResource(url);
        System.out.println(resource.getHttpHeaders().getContentType());
    }


}
