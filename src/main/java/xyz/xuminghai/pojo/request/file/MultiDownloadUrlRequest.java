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

package xyz.xuminghai.pojo.request.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import xyz.xuminghai.autoconfigure.TokenStatic;

import java.util.ArrayList;
import java.util.List;

/**
 * 2021/10/16 19:31 星期六<br/>
 * 多文件下载请求
 *
 * @author xuMingHai
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultiDownloadUrlRequest {

    /**
     * 压缩包名
     */
    private String archiveName;
    private List<DownloadInfo> downloadInfos;

    /**
     * 构建多文件下载请求
     *
     * @param archiveName 压缩包名
     * @param fileIdList  文件ID集合
     */
    public MultiDownloadUrlRequest(String archiveName, List<String> fileIdList) {
        this.archiveName = archiveName;
        List<DownloadInfo> list = new ArrayList<>(1);
        final DownloadInfo downloadInfo = new DownloadInfo();
        List<File> files = new ArrayList<>(fileIdList.size());
        fileIdList.forEach(s -> files.add(new File(s)));
        downloadInfo.setFiles(files);
        list.add(downloadInfo);
        this.downloadInfos = list;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class DownloadInfo {
        private String driveId = TokenStatic.DEFAULT_DRIVE_ID;
        private List<File> files;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class File {
        private String fileId;

        public File(String fileId) {
            this.fileId = fileId;
        }
    }


}
