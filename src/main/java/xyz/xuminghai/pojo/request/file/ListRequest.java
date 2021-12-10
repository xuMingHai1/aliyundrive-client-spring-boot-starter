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
import xyz.xuminghai.pojo.enums.OrderByEnum;
import xyz.xuminghai.pojo.enums.OrderDirectionEnum;

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
     * url过期时间
     */
    private int urlExpireSec = 1600;

    /**
     * 图片缩略图处理，具体细节可以看看阿里云OSS的图片处理参数
     */
    private String imageThumbnailProcess = "image/resize,w_400/format,jpeg";

    /**
     * 图片url处理
     */
    private String imageUrlProcess = "image/resize,w_1920/format,jpeg";

    /**
     * 视频缩略图过程
     */
    private String videoThumbnailProcess = "video/snapshot,t_0,f_jpg,ar_auto,w_300";

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

    public ListRequest() {
    }


}
