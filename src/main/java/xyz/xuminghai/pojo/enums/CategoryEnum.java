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

package xyz.xuminghai.pojo.enums;

import lombok.Getter;

/**
 * 文件搜索使用的文件类型，如图片，音频，视频，文件夹。。。
 *
 * @author xuMingHai
 */
@Getter
public enum CategoryEnum {

    /**
     * 所有类型
     */
    ALL(""),

    /**
     * 图片类型
     */
    IMAGE(" and category = \"image\""),

    /**
     * 视频类型
     */
    VIDEO(" and category = \"video\""),

    /**
     * 文件夹
     */
    FOLDER(" and type = \"folder\""),

    /**
     * 文档类型
     */
    DOC(" and category = \"doc\""),

    /**
     * 音频类型
     */
    AUDIO(" and category = \"audio\"");


    private final String value;

    CategoryEnum(String value) {
        this.value = value;
    }
}
