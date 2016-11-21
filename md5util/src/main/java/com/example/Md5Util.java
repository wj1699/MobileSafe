package com.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    public static String encoder(String pwd) {

        pwd = pwd + "mobilesafe";

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bs = digest.digest(pwd.getBytes());
            StringBuffer buffer = new StringBuffer();
            for (byte b : bs) {
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                buffer.append(hexString);
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
