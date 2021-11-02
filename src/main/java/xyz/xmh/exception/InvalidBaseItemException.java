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

package xyz.xmh.exception;

import xyz.xmh.pojo.entity.BaseItem;

/**
 * 2021/10/16 23:28 星期六<br/>
 * 这个baseItem是无效的，无法转换为子类出现的异常
 *
 * @author xuMingHai
 */
public class InvalidBaseItemException extends Exception {

    private final BaseItem baseItem;

    /**
     * 创建一个baseItem无法转为子类的异常
     *
     * @param baseItem 无法转换的baseItem
     * @param message  错误消息
     */
    public InvalidBaseItemException(BaseItem baseItem, String message) {
        super(message);
        this.baseItem = baseItem;
    }

    /**
     * 返回这个无效的baseItem
     *
     * @return baseItem
     */
    public BaseItem getBaseItem() {
        return baseItem;
    }
}
