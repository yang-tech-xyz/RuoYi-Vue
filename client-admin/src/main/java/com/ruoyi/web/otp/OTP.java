package com.ruoyi.web.otp;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class OTP {

    // 0 1 2 3 4 5 6 7 8
    private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    private static Base32 base32Inst = new Base32();

    // 最多可偏移的时间
    static int window_size = 3; // default 1 - max 17

    private OTP() {
    }

    /**
     * @param s
     */
    public void setWindowSize(int s) {
        if (s >= 1 && s <= 17)
            window_size = s;
    }

    /**
     * This method uses the JCE to provide the crypto algorithm. HMAC computes a
     * Hashed Message Authentication Code with the crypto hash algorithm as a
     * parameter.
     *
     * @param crypto   : the crypto algorithm (HmacSHA1, HmacSHA256, HmacSHA512)
     * @param keyBytes : the bytes to use for the HMAC key
     * @param text     : the message or text to be authenticated
     */
    private static byte[] hmacSha(String crypto, byte[] keyBytes, byte[] text) {
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

    /**
     * This method generates a TOTP value for the given set of parameters.
     *
     * @param key    : the shared secret
     * @param time   : a value that reflects a time
     * @param digits : number of digits to return
     * @return digits
     */
    public static int generateTOTP(String key, long time, int digits) {
        byte[] decodedKey = base32Inst.decode(key);
        byte[] msg = ByteBuffer.allocate(8).putLong(time).array();
        byte[] hash = hmacSha("HmacSHA1", decodedKey, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        return binary % DIGITS_POWER[digits];
    }

    public static String encodeBase32(byte[] bytes) {
        return base32Inst.encodeAsString(bytes);
    }

    /**
     * 柔性验证，加入偏移量延长时间
     *
     * @param secret
     * @param code
     * @param timeMsec
     * @return boolean
     */
    public static boolean checkCode(String secret, String code, long timeMsec) {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);
        long t = (timeMsec / 1000L) / 30L;
        for (int i = -window_size; i <= window_size; ++i) {
            String hash;
            try {
                hash = verifyCode(decodedKey, t + i);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
            if (StringUtils.equals(hash, code)) {
                // valid
                return true;
            }
        }
        // invalid
        return false;
    }

    /**
     * 验证
     *
     * @param key
     * @param t
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    private static String verifyCode(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);
        int offset = hash[20 - 1] & 0xF;
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;
        // 处理0开头的数值
        String code = String.valueOf(truncatedHash);
        return code.length() == 5 ? "0".concat(code) : code;
    }
}
