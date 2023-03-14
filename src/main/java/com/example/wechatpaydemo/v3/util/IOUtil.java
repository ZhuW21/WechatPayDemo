package com.example.wechatpaydemo.v3.util;

import java.io.*;

/**
 * IO工具类
 *
 * @author qingzhou
 * @date 2023-03-14 16:49
 **/
public class IOUtil {

    /**
     * 数据流末尾
     */
    private static final int EOF = -1;

    /**
     * inputStream写入文件
     *
     * @param inputStream 数据流
     * @param targetPath  文件路径
     */
    public static void backUpFile(InputStream inputStream, String targetPath) {

        if (inputStream == null) {
            throw new NullPointerException("数据流不能为空");
        }

        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(targetPath))
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
