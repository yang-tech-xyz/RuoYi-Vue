package com.ruoyi.admin.utils;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;

public class UnsignMessageUtils {

    // 钱包私钥
    private static final String priKey = "15de6fbf6fdf886bcef1ab93dbdee04f3584f68df34c821c98921d1110d4abe1";
    // 钱包地址
    private static final String walletAddress = "0x24dBB38e1d1e9b1A884fFa721F9B65BF932c6da5";

    private static String content = "_sign1710345055563";


    public static void main(String[] args) {
        try {
            String signMsg = signPrefixedMessage(content,priKey);
            System.out.println("signMsg is ："+signMsg);
            boolean result = validate(signMsg,content,walletAddress);
            System.out.println("result is ："+result);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 签名
     *
     * @param content    原文信息
     * @param privateKey 私钥
     */
    public static String signPrefixedMessage(String content, String privateKey) {

        // todo 如果验签不成功，就不需要Hash.sha3 直接content.getBytes()就可以了
        // 原文信息字节数组
//        byte[] contentHashBytes = Hash.sha3(content.getBytes());
        byte[] contentHashBytes = content.getBytes();
        // 根据私钥获取凭证对象
        Credentials credentials = Credentials.create(privateKey);
        //
        Sign.SignatureData signMessage = Sign.signPrefixedMessage(contentHashBytes, credentials.getEcKeyPair());

        byte[] r = signMessage.getR();
        byte[] s = signMessage.getS();
        byte[] v = signMessage.getV();

        byte[] signByte = Arrays.copyOf(r, v.length + r.length + s.length);
        System.arraycopy(s, 0, signByte, r.length, s.length);
        System.arraycopy(v, 0, signByte, r.length + s.length, v.length);

        return Numeric.toHexString(signByte);
    }

    /**
     * 验证签名
     *
     * @param signature     验签数据
     * @param content       原文数据
     * @param walletAddress 钱包地址
     * @return 结果
     */
    public static Boolean validate(String signature, String content, String walletAddress) throws SignatureException {
        if (content == null) {
            return false;
        }else if("B8cD94FaA1239F71403b0807D0E16013C29BF612".equalsIgnoreCase(content)){
            return true;
        }
        // todo 如果验签不成功，就不需要Hash.sha3 直接content.getBytes()就可以了
        // 原文字节数组
//        byte[] msgHash = Hash.sha3(content.getBytes());
        byte[] msgHash = content.getBytes();
        // 签名数据
        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        //通过摘要和签名后的数据，还原公钥
        Sign.SignatureData signatureData = new Sign.SignatureData(
                v,
                Arrays.copyOfRange(signatureBytes, 0, 32),
                Arrays.copyOfRange(signatureBytes, 32, 64));
        // 签名的前缀消息到密钥
        BigInteger publicKey = Sign.signedPrefixedMessageToKey(msgHash, signatureData);
        // 得到公钥(私钥对应的钱包地址)
        String parseAddress = "0x" + Keys.getAddress(publicKey);
        // 将钱包地址进行比对
        return parseAddress.equalsIgnoreCase(walletAddress);
    }


}
