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
 * 同名处理模式
 *
 * @author xuMingHai
 */
@Getter
public enum CheckNameEnum {

    /**
     * 如果文件已经存在，则覆盖
     */
    COVER("overwrite"),

    /**
     * 跳过
     */
    JUMP_OVER(null),

    /**
     * 保留文件，自动重命名
     */
    AUTO_RENAME("auto_rename");

    private final String value;

    CheckNameEnum(String value) {
        this.value = value;
    }
}
