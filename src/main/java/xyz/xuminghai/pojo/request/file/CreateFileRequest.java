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
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

/**
 * 2021/10/18 3:41 星期一<br/>
 * 创建文件请求参数
 *
 * @author xuMingHai
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateFileRequest extends CreateFolderRequest {

    /**
     * 类型是文件
     */
    private final String type = "file";
    /**
     * hash算法名，暂时支持sha1
     */
    private final String contentHashName = "sha1";
    /**
     * 版本
     */
    private final String proofVersion = "v1";
    /**
     * 文件大小
     */
    private long size;
    /**
     * 文件hash，文件长度为0使用
     */
    private String contentHash = "DA39A3EE5E6B4B0D3255BFEF95601890AFD80709";
    /**
     * 分段上传
     */
    private List<PartInfo> partInfoList = Collections.singletonList(new PartInfo(1));
    /**
     * 文件校验码
     */
    private String proofCode;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PartInfo {
        private int partNumber;

        public PartInfo(int partNumber) {
            this.partNumber = partNumber;
        }
    }


}
