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

package xyz.xuminghai.pojo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 最基础的文件属性父类<br/>
 * 继承顺序：BaseItem <- Item <- 其他类型的文件<br/>
 * 如果是文件夹可以用这个类保存信息
 *
 * @author xuMingHai
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "category", visible = true, defaultImpl = BaseItem.class)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = ImageItem.class, name = "image"),
        @JsonSubTypes.Type(value = VideoItem.class, name = "video"),
        @JsonSubTypes.Type(value = AudioItem.class, name = "audio"),
        @JsonSubTypes.Type(value = DocItem.class, name = "doc"),
        @JsonSubTypes.Type(value = Item.class, name = "others")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseItem {

    /**
     * 阿里云ID
     */
    private String driveId;

    /**
     * 域ID
     */
    private String domainId;

    /**
     * 文件ID
     */
    private String fileId;

    /**
     * 文件名
     */
    private String name;

    /**
     * 类型，区分是文件还是文件夹
     */
    private String type;

    /**
     * 创建于
     */
    private LocalDateTime createdAt;

    /**
     * 更新于
     */
    private LocalDateTime updatedAt;

    /**
     * 隐藏
     */
    private boolean hidden;

    private boolean starred;

    /**
     * 状态
     */
    private String status;

    /**
     * 父文件ID
     */
    private String parentFileId;

    /**
     * 加密模式
     */
    private String encryptMode;

    /**
     * 最后修改ID
     */
    private String lastModifierId;

    /**
     * 做最后修改的用户或者其他的名称
     */
    private String lastModifierName;

    /**
     * 最后修改的类型，是用户还是其他
     */
    private String lastModifierType;

    /**
     * 用户使用的什么客户端
     */
    private String userMeta;

}
