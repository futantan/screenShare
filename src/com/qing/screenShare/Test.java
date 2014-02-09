package com.qing.screenShare;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tan on 14-2-9.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        String fileName = "D:" + File.separator + "hello";
        File file = new File(fileName);
        InputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        inputStream.read(bytes);
        System.out.println("文件长度为:" + file.length());
        inputStream.close();
        System.out.println(new String(bytes));
    }
}
