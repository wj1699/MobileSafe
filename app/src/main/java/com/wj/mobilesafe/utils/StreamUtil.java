package com.wj.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
    /**
     * 流转字符串
     *
     * @param in 接收的输入流
     * @return 返回流转换成的字符串
     * @throws IOException 抛出的异常类型
     */
    public static String StreamToString(InputStream in) throws IOException {
        //读取到的数据存储到一个缓存空间然后一次性写出作为一个字符串返回
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int temp = -1;

        while ((temp = in.read(buffer)) != -1) {
            //还有数据
            bos.write(buffer, 0, temp);
        }
        String result = bos.toString();
        in.close();
        bos.close();
        return result;
    }
}
