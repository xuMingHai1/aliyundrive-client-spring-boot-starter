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

import lombok.extern.slf4j.Slf4j;
import xyz.xuminghai.exception.UnsupportedSystemException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractList;
import java.util.List;
import java.util.Locale;

/**
 * 2021/10/18 20:30 星期一<br/>
 * 调用操作系统方法，计算文件hash
 *
 * @author xuMingHai
 */
@Slf4j
public final class HashUtils {

    /**
     * 受支持的操作系统
     */
    private static final List<String> LIST = new AbstractList<String>() {

        private final String[] strings = {"Windows", "Linux", "Mac"};

        @Override
        public int size() {
            return strings.length;
        }

        @Override
        public String get(int index) {
            return strings[index];
        }
    };

    /**
     * 当前操作系统的名字
     */
    private static final String SYSTEM_NAME = System.getProperty("os.name");

    private HashUtils() {
    }

    /**
     * 获取文件的sha1值
     *
     * @param path 文件路径
     * @return 转为大写后的sha1
     */
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static String sha1(Path path) {
        checkFile(path);
        log.info("开始计算【{}】的sha1", path.getFileName());
        if (SYSTEM_NAME.contains(LIST.get(0))) {
            return windowsSha1(path.toAbsolutePath().toString()).toUpperCase(Locale.ROOT);
        } else if (SYSTEM_NAME.contains(LIST.get(1))) {
            return linuxSha1(path.toAbsolutePath().toString()).toUpperCase(Locale.ROOT);
        } else if (SYSTEM_NAME.contains(LIST.get(2))) {
            return macSha1(path.toAbsolutePath().toString()).toUpperCase(Locale.ROOT);
        } else {
            throw new UnsupportedSystemException("当前操作系统还不支持");
        }

    }

    /**
     * 获取文件的md5值
     *
     * @param path 文件路径
     * @return 转为大写后的md5
     */
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static String md5(Path path) {
        checkFile(path);
        log.info("开始计算【{}】的md5", path.getFileName());
        if (SYSTEM_NAME.contains(LIST.get(0))) {
            return windowsMd5(path.toAbsolutePath().toString()).toUpperCase(Locale.ROOT);
        } else if (SYSTEM_NAME.contains(LIST.get(1))) {
            return linuxMd5(path.toAbsolutePath().toString()).toUpperCase(Locale.ROOT);
        } else if (SYSTEM_NAME.contains(LIST.get(2))) {
            return macMd5(path.toAbsolutePath().toString()).toUpperCase(Locale.ROOT);
        } else {
            throw new UnsupportedSystemException("当前操作系统还不支持");
        }
    }

    private static void checkFile(Path path) {

        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("这个路径不是文件：" + path);
        }

        if (!Files.exists(path)) {
            throw new FileSystemNotFoundException("没有找到这个文件：" + path);
        }

        try {
            if (Files.size(path) == 0) {
                throw new IllegalArgumentException("不支持文件大小为0个字节：" + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 返回不可修改的支持的操作系统集合
     *
     * @return 操作系统名字集合
     */
    public static List<String> supportedSystem() {
        return LIST;
    }

    private static String windowsSha1(String path) {
        final ProcessBuilder processBuilder = new ProcessBuilder("CertUtil", "-hashfile", path, "SHA1");
        return windowsExecuteCommand(processBuilder);
    }

    private static String windowsMd5(String path) {
        final ProcessBuilder processBuilder = new ProcessBuilder("CertUtil", "-hashfile", path, "MD5");
        return windowsExecuteCommand(processBuilder);
    }

    private static String windowsExecuteCommand(ProcessBuilder processBuilder) {
        try {
            final Process process = processBuilder.start();
            final InputStream inputStream = process.getInputStream();
            final String string = IoUtils.toString(IoUtils.printlnResetOrPushback(inputStream, Charset.forName("GBK")), Charset.forName("GBK"));
            inputStream.close();
            return string.split("哈希:")[1].split("CertUtil:")[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String linuxSha1(String path) {
        final ProcessBuilder processBuilder = new ProcessBuilder("sha1sum", "-b", path);
        return linuxExecuteCommand(processBuilder);
    }

    private static String linuxMd5(String path) {
        final ProcessBuilder processBuilder = new ProcessBuilder("md5sum", "-b", path);
        return linuxExecuteCommand(processBuilder);
    }

    private static String linuxExecuteCommand(ProcessBuilder processBuilder) {
        try {
            final Process process = processBuilder.start();
            final InputStream inputStream = process.getInputStream();
            final String string = IoUtils.toString(IoUtils.printlnResetOrPushback(inputStream, StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            inputStream.close();
            return string.split(" ")[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String macSha1(String path) {
        // TODO: 暂无Mac
        return "";
    }

    private static String macMd5(String path) {
        // TODO: 暂无Mac
        return "";
    }
}
