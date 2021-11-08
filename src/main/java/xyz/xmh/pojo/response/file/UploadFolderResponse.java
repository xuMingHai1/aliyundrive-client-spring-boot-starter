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
import lombok.Data;

import java.io.Serializable;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

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
    private abstract static class AbstractFile implements Serializable {
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

        /**
         * 上传文件所在的本地系统的路径
         */
        private Path path;

        /**
         * 父目录的引用
         */
        private Folder parentFolder;

        /**
         * 上传开始时间，在创建对象时赋值
         */
        private final LocalDateTime uploadStartTime;

        /**
         * 上传结束时间
         */
        private LocalDateTime uploadEndTime;

        /**
         * 父目录的hashCode，防止子类调用toString时导致递归问题
         */
        private transient int parentFolderHashCode;

        public String getType() {
            return type;
        }

        public String getParentFileId() {
            return parentFileId;
        }

        public void setParentFileId(String parentFileId) {
            this.parentFileId = parentFileId;
        }

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Path getPath() {
            return path;
        }

        public void setPath(Path path) {
            this.path = path;
        }

        public Folder getParentFolder() {
            return parentFolder;
        }

        public void setParentFolder(Folder parentFolder) {
            this.parentFolder = parentFolder;
            Optional.ofNullable(parentFolder).ifPresent(folder1 -> this.parentFolderHashCode = folder1.hashCode());
        }

        public LocalDateTime getUploadStartTime() {
            return uploadStartTime;
        }

        public LocalDateTime getUploadEndTime() {
            return uploadEndTime;
        }

        public void setUploadEndTime(LocalDateTime uploadEndTime) {
            this.uploadEndTime = uploadEndTime;
        }

        protected AbstractFile(String type) {
            this.type = type;
            this.uploadStartTime = LocalDateTime.now();
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", "", "")
                    .add("type='" + type + "'")
                    .add("parentFileId='" + parentFileId + "'")
                    .add("fileId='" + fileId + "'")
                    .add("fileName='" + fileName + "'")
                    .add("path=" + path)
                    /*
                        防止递归过深，出现StackOverflowError
                        使用临时的变量，预防父目录为null调用hashCode方法出现异常
                     */
                    .add("parentFolderHashCode=" + parentFolderHashCode)
                    .add("uploadStartTime=" + uploadStartTime)
                    .add("uploadEndTime=" + uploadEndTime)
                    .toString();
        }
    }

    /**
     * 文件夹类
     */
    public final static class Folder extends AbstractFile {

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

        public List<Folder> getFolderList() {
            return folderList;
        }

        public void setFolderList(List<Folder> folderList) {
            this.folderList = folderList;
        }

        public List<File> getFileList() {
            return fileList;
        }

        public void setFileList(List<File> fileList) {
            this.fileList = fileList;
        }


        @Override
        public String toString() {
            return new StringJoiner(", ", Folder.class.getSimpleName() + "[", "]")
                    .add(super.toString())
                    .add("folderList.size()=" + Optional.ofNullable(folderList).orElse(Collections.emptyList()).size())
                    .add("fileList.size()=" + Optional.ofNullable(fileList).orElse(Collections.emptyList()).size())
                    .toString();
        }


        /**
         * 给当前文件夹添加文件夹<br/>
         * 如果文件夹集合还未初始化会先初始化再添加，如果已经初始化，则直接添加
         * @param folder 文件夹类
         */
        public void addFolder(Folder folder) {
            Objects.requireNonNull(folder, "添加的文件夹类不能为null");
            folderList = Optional.ofNullable(folderList).orElse(new ArrayList<>());
            folderList.add(folder);
        }

        /**
         * 给当前文件夹添加文件类<br/>
         * 如果文件集合还未初始化会先初始化再添加，如果已经初始化，则直接添加
         * @param file 要添加的文件类
         */
        public void addFile(File file) {
            Objects.requireNonNull(file, "添加的文件类不能为null");
            fileList = Optional.ofNullable(fileList).orElse(new ArrayList<>());
            fileList.add(file);
        }

    }

    /**
     * 文件类
     */
    public final static class File extends AbstractFile {

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

        public boolean isRapidUpload() {
            return rapidUpload;
        }

        public void setRapidUpload(boolean rapidUpload) {
            this.rapidUpload = rapidUpload;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", File.class.getSimpleName() + "[", "]")
                    .add(super.toString())
                    .add("rapidUpload=" + rapidUpload)
                    .toString();
        }
    }

}
