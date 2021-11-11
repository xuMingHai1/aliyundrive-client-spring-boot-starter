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

package xyz.xuminghai.util;

import xyz.xuminghai.autoconfigure.TokenStatic;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

/**
 * 2021/10/20 4:51 星期三<br/>
 * 阿里云盘文件证明，v1版
 *
 * @author xuMingHai
 * @see <a href="https://github.com/liupan1890/aliyunpan/issues/319">感谢原作者</a>
 */
public final class ProofV1Utils {

    private ProofV1Utils() {
    }

    /**
     * 获取证明码
     *
     * @param path 要上传的文件路径
     * @return proof_code
     */
    public static String getCode(Path path) {
        long size = 0;
        try {
            size = Files.size(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final BigInteger token = new BigInteger(Md5Utils.digest(TokenStatic.ACCESS_TOKEN.getBytes()).substring(0, 16), 16);
        final long start = token.remainder(new BigInteger(Long.toString(size))).longValue();
        final long end = Math.min(start + 8, size);

        final byte[] bytes = new byte[Math.toIntExact(end - start)];
        try {
            final RandomAccessFile randomAccessFile = new RandomAccessFile(path.toFile(), "r");
            randomAccessFile.seek(start);
            randomAccessFile.read(bytes);
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(bytes);
    }


}
