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

package xyz.xuminghai.api;

import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 2021/10/10 13:59 星期日<br/>
 * 文件api
 *
 * @author xuMingHai
 */
@Getter
public enum FileEnum {

    /**
     * 查询指定文件
     */
    GET(getUri("https://api.aliyundrive.com/v2/file/get"), HttpMethod.POST),

    /**
     * 获取指定文件的下载地址
     */
    GET_DOWNLOAD_URL(getUri("https://api.aliyundrive.com/v2/file/get_download_url"), HttpMethod.POST),

    /**
     * 查询指定目录的文件
     */
    LIST(getUri("https://api.aliyundrive.com/adrive/v3/file/list"), HttpMethod.POST),

    /**
     * 搜索文件
     */
    SEARCH(getUri("https://api.aliyundrive.com/adrive/v3/file/search"), HttpMethod.POST),

    /**
     * 多个文件下载
     */
    MULTI_DOWNLOAD_URL(getUri("https://api.aliyundrive.com/adrive/v1/file/multiDownloadUrl"), HttpMethod.POST),

    /**
     * 创建文件夹或文件
     */
    CREATE_WITH_FOLDERS(getUri("https://api.aliyundrive.com/adrive/v2/file/createWithFolders"), HttpMethod.POST),

    /**
     * 修改文件名
     */
    UPDATE(getUri("https://api.aliyundrive.com/v3/file/update"), HttpMethod.POST),

    /**
     * 上传文件后，完全性检查
     */
    COMPLETE(getUri("https://api.aliyundrive.com/v2/file/complete"), HttpMethod.POST);


    /**
     * api接口
     */
    private final URI api;

    /**
     * 请求方式
     */
    private final HttpMethod httpMethod;

    FileEnum(URI api, HttpMethod httpMethod) {
        this.api = api;
        this.httpMethod = httpMethod;
    }

    static URI getUri(String str) {
        try {
            return new URI(str);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
