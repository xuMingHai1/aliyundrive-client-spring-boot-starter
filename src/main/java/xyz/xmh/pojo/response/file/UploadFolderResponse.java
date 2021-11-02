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

package xyz.xmh.pojo.response.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

/**
 * 2021/11/2 13:38 星期二<br/>
 * 上传文件夹响应类， 使用组合模式，模拟文件夹
 *
 * @author xuMingHai
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UploadFolderResponse {

    /**
     * 文件夹
     */
    private Folder folder;

    /**
     * 文件抽象类，定义了文件夹和文件的共同属性
     */
    @Data
    abstract static class AbstractFile {
        /**
         * 明确的类型，文件夹或文件
         */
        private final String type;
        /**
         * 父目录ID
         */
        private String parentFileId;
        /**
         * 自己的ID
         */
        private String fileId;
        /**
         * 文件名
         */
        private String fileName;

        AbstractFile(String type) {
            this.type = type;
        }

    }

    /**
     * 文件夹类
     */
    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class Folder extends AbstractFile {

        /**
         * 文件夹中存在的文件夹
         */
        private List<Folder> folderList;

        /**
         * 文件夹中存在的文件
         */
        private List<File> fileList;

        /**
         * 文件夹的构造器
         */
        public Folder() {
            super("folder");
        }

    }

    /**
     * 文件类
     */
    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class File extends AbstractFile {

        /**
         * 是否快传
         */
        private boolean rapidUpload;

        /**
         * 文件的构造器
         */
        public File() {
            super("file");
        }

    }

}
