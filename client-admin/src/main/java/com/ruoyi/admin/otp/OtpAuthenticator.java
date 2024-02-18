package com.ruoyi.admin.otp;

import org.apache.commons.lang3.BooleanUtils;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class OtpAuthenticator {

    private static final String URL_FORMAT = "otpauth://totp/%s?secret=%s&issuer=%s";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 调用改方法返回密钥与URL参数
     * 返回MAP(secretKey=密钥，qrCode=二维码URL)
     */
    public static Map<String, String> getSecretParameters(String mebId, String secretKey, String remark) {
        Map<String, String> result = new HashMap<>();
        result.put("secretKey", secretKey);
        result.put("qrCode", String.format(URL_FORMAT, mebId, secretKey, remark.isEmpty() ? "云算力" : remark));
        return result;
    }

    /**
     * 校验输入的六位数码是否正确
     * 合格返回TRUE,反之FALSE
     */
    public static Boolean checkCode(String googleAuthKey, String code) {
        boolean flag = OTP.checkCode(googleAuthKey, code, System.currentTimeMillis());
        return BooleanUtils.isTrue(flag);
    }

    public static String secretKey() {
        byte[] keybuf = new byte[10];
        RANDOM.nextBytes(keybuf);
        return OTP.encodeBase32(keybuf);
    }

    /**
     * 随机数字
     * 1.digits 小数位
     */
    public static String generateCode(int digits) {
        byte[] bytes = new byte[10];
        RANDOM.nextBytes(bytes);
        String key = OTP.encodeBase32(bytes);
        return String.valueOf(OTP.generateTOTP(key, System.currentTimeMillis(), digits));
    }
}