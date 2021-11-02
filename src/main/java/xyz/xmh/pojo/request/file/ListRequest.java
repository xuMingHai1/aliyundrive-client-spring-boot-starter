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

package xyz.xmh.pojo.request.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import xyz.xmh.autoconfigure.TokenStatic;
import xyz.xmh.pojo.enums.OrderByEnum;
import xyz.xmh.pojo.enums.OrderDirectionEnum;

/**
 * 文件列表请求参数
 *
 * @author xuMingHai
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListRequest {

    /**
     * 驱动ID
     */
    private String driveId = TokenStatic.DEFAULT_DRIVE_ID;

    /**
     * 父文件ID，root是顶级目录
     */
    private String parentFileId = "root";

    /**
     * 范围
     */
    private int limit = 100;

    /**
     * 是否全部
     */
    private boolean all = false;

    /**
     * 文件下载的url过期时间
     */
    private int urlExpireSec = 1600;

    /**
     * 图像缩略图过程
     */
    private String imageThumbnailProcess = "image/resize,w400/format,jpeg";

    /**
     * 图片网址处理
     */
    private String imageUrlProcess = "image/resize,w1920/format,jpeg";

    /**
     * 视频缩略图过程
     */
    private String videoThumbnailProcess = "video/snapshot,t0,fJpg,arAuto,w300";

    /**
     * 领域
     */
    private String fields = "*";

    /**
     * 排序方式，默认更新时间
     */
    private String orderBy = OrderByEnum.UPDATE_AT.getValue();

    /**
     * 排序方向，默认降序
     */
    private String orderDirection = OrderDirectionEnum.DESC.getValue();

    public void setOrderBy(OrderByEnum orderBy) {
        this.orderBy = orderBy.getValue();
    }

    public void setOrderDirection(OrderDirectionEnum orderDirection) {
        this.orderDirection = orderDirection.getValue();
    }
}
