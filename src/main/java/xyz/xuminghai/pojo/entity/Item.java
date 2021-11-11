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

package xyz.xuminghai.pojo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import xyz.xuminghai.exception.InvalidBaseItemException;

import java.net.URL;
import java.util.Objects;

/**
 * 文件基本属性<br/>
 * 如果是其他类型的文件，使用这个类保存信息
 *
 * @author xuMingHai
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item extends BaseItem {

    /**
     * 内容类型
     */
    private String contentType;

    /**
     * 文件扩展名
     */
    private String fileExtension;

    /**
     * mimeType
     */
    private String mimeType;

    /**
     * mime扩展名
     */
    private String mimeExtension;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 上传ID
     */
    private String uploadId;

    /**
     * 循环冗余校验（英语：Cyclic redundancy check，通称“CRC”）<br/>
     * 是一种根据网络数据包或电脑文件等数据产生简短固定位数校验码的一种散列函数，<br/>
     * 主要用来检测或校验数据传输或者保存后可能出现的错误。生成的数字在传输或者存储之前计算出来并且附加到数据后面，<br/>
     * 然后接收方进行检验确定数据是否发生变化。由于本函数易于用二进制的电脑硬件使用、<br/>
     * 容易进行数学分析并且尤其善于检测传输通道干扰引起的错误，因此获得广泛应用。<br/>
     * 此方法是由W. Wesley Peterson于1961年发表[1]。
     */
    private String crc64Hash;
    private String contentHash;
    private String contentHashName;

    /**
     * 显示文件的url
     */
    private URL url;

    /**
     * 下载地址
     */
    private URL downloadUrl;

    /**
     * 类别（如图片，视频）
     */
    private String category;
    private int punishFlag;

    /**
     * 将BaseItem转为Item<br/>
     * 如果你不是很确定这个baseItem可以转为item，请用try包裹这个方法，在catch块调用{@link InvalidBaseItemException#getBaseItem()}
     * 来使用无效的baseItem
     *
     * @param baseItem 要做转换的baseItem
     * @return {@link Item}
     */
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static Item valueOf(BaseItem baseItem) throws InvalidBaseItemException {
        if ("file".equals(Objects.requireNonNull(baseItem, "baseItem不能为null").getType())) {
            return (Item) baseItem;
        }
        throw new InvalidBaseItemException(baseItem, "这个baseItem的类型不是文件");
    }
}
