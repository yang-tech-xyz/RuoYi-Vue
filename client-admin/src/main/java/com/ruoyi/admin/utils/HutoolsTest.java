package com.ruoyi.admin.utils;

import cn.hutool.Hutool;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;

public class HutoolsTest {
    public static void main(String[] args) {
        String text = "58eef556f93ba37d103e48867fd69e4b477f19e6df2d485d89aee0e0d3c3cbec";
        // key：AES模式下，key必须为16位
        String key = "abcdefghijklmnopqrstuvwxyz".substring(0,16);
        // iv：偏移量，ECB模式不需要，CBC模式下必须为16位
        String iv = "1234567812345678";
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());
        // 加密并进行Base转码
        String encrypt = aes.encryptBase64(text);
        System.out.println(encrypt);
        // 解密为字符串
        String decrypt = aes.decryptStr(encrypt);
        System.out.println(decrypt);
    }
}
