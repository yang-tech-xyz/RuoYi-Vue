package com.ruoyi.web.utils;

import cn.hutool.Hutool;
import cn.hutool.crypto.SecureUtil;

public class HutoolsTest {
    public static void main(String[] args) {
        byte[] encrypt = SecureUtil.aes().encrypt("0x234kdfjasdlkfhjalskdjfas");
        byte[] decrypt = SecureUtil.aes().decrypt(encrypt);
        String result = new String(decrypt);
        System.out.println("result is:"+result);
    }
}
