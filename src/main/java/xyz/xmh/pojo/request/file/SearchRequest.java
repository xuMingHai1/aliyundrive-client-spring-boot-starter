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
import xyz.xmh.pojo.enums.CategoryEnum;
import xyz.xmh.pojo.enums.OrderByEnum;
import xyz.xmh.pojo.enums.OrderDirectionEnum;

/**
 * 搜索文件请求参数
 *
 * @author xuMingHai
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchRequest {

    private String driveId = TokenStatic.DEFAULT_DRIVE_ID;
    private int limit = 100;
    private String query = "name match \"\"";
    private String imageThumbnailProcess = "image/resize,w200/format,jpeg";
    private String imageUrlProcess = "image/resize,w1920/format,jpeg";
    private String videoThumbnailProcess = "video/snapshot,t0,fJpg,arAuto,w300";
    private String orderBy = OrderByEnum.UPDATE_AT.getValue() + " " + OrderDirectionEnum.DESC.getValue();

    /**
     * 设置要搜索的文件名，不指定文件类型
     *
     * @param query 文件名
     */
    public void setQuery(String query) {
        this.query = "name match \"" + query + "\"";
    }

    /**
     * 设置要搜索的文件名，指定文件类型
     *
     * @param query        文件名
     * @param categoryEnum 文件类型
     */
    public void setQuery(String query, CategoryEnum categoryEnum) {
        this.query = "name match \"" + query + "\"" + categoryEnum.getValue();
    }

    /**
     * 设置排序方式
     *
     * @param orderByEnum        根据什么排序，如名称、创建时间。。。
     * @param orderDirectionEnum 根据什么排序，如名称、创建时间。。。
     */
    public void setOrderBy(OrderByEnum orderByEnum, OrderDirectionEnum orderDirectionEnum) {
        this.orderBy = orderByEnum.getValue() + " " + orderDirectionEnum.getValue();
    }
}
