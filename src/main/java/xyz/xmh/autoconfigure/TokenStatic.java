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

package xyz.xmh.autoconfigure;

import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 令牌信息，根据token值获取，反射赋值 <br/>
 * （使用对象，改变引用才能反射赋值public static final）
 *
 * @author xuMingHai
 */
@ToString
public final class TokenStatic {

    /**
     * 默认阿里云盘ID
     */
    public static final String DEFAULT_DRIVE_ID = new String("85455411");

    /**
     * 用户名
     */
    public static final String USER_NAME = new String();

    /**
     * 角色
     */
    public static final String ROLE = new String();

    /**
     * 令牌过期时间
     */
    public static final LocalDateTime EXPIRE_TIME = LocalDateTime.now();

    /**
     * 令牌类型
     */
    public static final String TOKEN_TYPE = new String();

    /**
     * 访问令牌
     */
    public static final String ACCESS_TOKEN = new String();

    /**
     * 刷新令牌
     */
    public static final String REFRESH_TOKEN = new String();

    /**
     * 用户ID
     */
    public static final String USER_ID = new String();

    /**
     * 过期秒数
     */
    public static final Integer EXPIRES_IN = 7200;

    public static final String DEFAULT_SBOX_DRIVE_ID = new String();

    /**
     * 设备ID
     */
    public static final String DEVICE_ID = new String();

    /**
     * 域名
     */
    public static final String DOMAIN_ID = new String();

    /**
     * 头像地址
     */
    public static final String AVATAR = new String();

    /**
     * 状态
     */
    public static final String STATE = new String();

    /**
     * 地位
     */
    public static final String STATUS = new String();

    /**
     * 昵称
     */
    public static final String NICK_NAME = new String();

    /**
     * 需要链接
     */
    public static final Boolean NEED_LINK = false;

    /**
     * 引用设置
     */
    public static final Boolean PIN_SETUP = false;

    /**
     * 需要RP验证
     */
    public static final Boolean NEED_RP_VERIFY = false;

    /**
     * 第一次登录
     */
    public static final Boolean IS_FIRST_LOGIN = false;

    /**
     * 现有链接
     */
    public static final List<String> EXIST_LINK = new ArrayList<>();


}
