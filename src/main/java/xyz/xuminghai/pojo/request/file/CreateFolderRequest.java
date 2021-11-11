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
import xyz.xuminghai.pojo.enums.CheckNameEnum;

/**
 * 2021/10/18 3:30 星期一<br/>
 * 创建文件夹请求参数
 *
 * @author xuMingHai
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateFolderRequest {

    /**
     * 类型是文件夹
     */
    private final String type = "folder";
    /**
     * 同名处理模式，默认自动重命名
     */
    private String checkNameMode = CheckNameEnum.AUTO_RENAME.getValue();
    /**
     * 阿里云盘ID
     */
    private String driveId = TokenStatic.DEFAULT_DRIVE_ID;
    /**
     * 父目录ID，默认使用顶级目录
     */
    private String parentFileId = "root";
    /**
     * 文件名
     */
    private String name;

    public void setCheckNameMode(CheckNameEnum checkNameMode) {
        this.checkNameMode = checkNameMode.getValue();
    }
}
