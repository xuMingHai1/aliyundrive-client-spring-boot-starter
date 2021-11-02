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

package xyz.xmh.pojo.enums;

import lombok.Getter;

/**
 * 根据什么排序，如名称、创建时间。。。
 *
 * @author xuMingHai
 */
@Getter
public enum OrderByEnum {

    /**
     * 名称
     */
    NAME("name"),

    /**
     * 创建时间
     */
    CREATE_AT("created_at"),

    /**
     * 更新时间
     */
    UPDATE_AT("updated_at"),

    /**
     * 文件大小
     */
    SIZE("size");


    private final String value;

    OrderByEnum(String value) {
        this.value = value;
    }


}
