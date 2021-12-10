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

package xyz.xuminghai.exception;

/**
 * 2021/12/3 8:42 星期五<br/>
 * 阿里云盘资源过期，当资源的时间戳已经到了，表示这个资源已经过期无法访问
 * @author xuMingHai
 */
public class AlyunResourceExpiredException extends RuntimeException{

    public AlyunResourceExpiredException(String message) {
        super(message);
    }
}
