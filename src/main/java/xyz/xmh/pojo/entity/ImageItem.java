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

package xyz.xmh.pojo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import xyz.xmh.exception.InvalidBaseItemException;
import xyz.xmh.pojo.entity.image.ImageMediaMetadata;

import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * 图片类型的文件
 *
 * @author xuMingHai
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageItem extends Item {

    /**
     * 标签
     */
    private List<String> labels;

    /**
     * 缩略图
     */
    private URL thumbnail;

    /**
     * 图像媒体的元数据
     */
    private ImageMediaMetadata imageMediaMetadata;

    /**
     * 将BaseItem转为ImageItem<br/>
     * 如果你不是很确定这个baseItem可以转为ImageItem，请用try包裹这个方法，在catch块调用{@link InvalidBaseItemException#getBaseItem()}
     * 来使用无效的baseItem
     *
     * @param baseItem 要做转换的baseItem
     * @return {@link ImageItem}
     */
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static ImageItem valueOf(BaseItem baseItem) throws InvalidBaseItemException {
        if ("file".equals(Objects.requireNonNull(baseItem, "baseItem不能为null").getType())) {
            Item item = (Item) baseItem;
            if ("image".equals(item.getCategory())) {
                return (ImageItem) baseItem;
            }
            throw new InvalidBaseItemException(baseItem, "这个baseItem的类别不是图片");
        }
        throw new InvalidBaseItemException(baseItem, "这个baseItem的类型不是文件");
    }

}
