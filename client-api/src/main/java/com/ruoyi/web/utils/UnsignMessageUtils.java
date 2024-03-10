package com.ruoyi.web.utils;

import org.bouncycastle.util.encoders.Hex;
import org.tron.trident.core.key.KeyPair;
import org.tron.trident.core.transaction.SignatureValidator;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;

public class UnsignMessageUtils {

    // 钱包私钥
    private static final String priKey = "e62248374af86aa480f9cebd44f04cd02b915130d4fbda885a201488257b0a17";
    // 钱包地址
    private static final String walletAddress = "0x5ebacac108d665819398e5c37e12b0162d781398";

    private static String content = "青年人的责任重大！努力吧...";


    public static void main(String[] args) {
        try {
            validateTronTest();
//            String signMsg = signPrefixedMessage(content,priKey);
//            System.out.println("signMsg is ："+signMsg);
//            boolean result = validate(signMsg,content,walletAddress);
//            System.out.println("result is ："+result);
        } catch (Exception e) {
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
        }
        //验证tron钱包
        if(!"GatPool-sign".equalsIgnoreCase(content)){
            return SignatureValidator.verify(content,signature,walletAddress);
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



    public static boolean validateTronTest(){
        KeyPair keyPair = new KeyPair("dcfc65fc8628612bd47cb5ebbbf495046134b46b98743289f14a641aea3a5fc6");
        String txid = "3f41ea1947027fd0b30f32f1fdddf3236f00fbb090a5223a1888c74995ea70e9";
        System.out.println("签名原信息:");
        System.out.println(txid);
        byte[] signature = KeyPair.signTransaction(Hex.decode(txid), keyPair);
        String signatureValue = new String(Hex.encode(signature));
        System.out.println("签名信息:");
        System.out.println(signatureValue);
        String hexAddress = keyPair.toHexAddress();
        System.out.println("钱包地址:");
        System.out.println(hexAddress);
        boolean verify = SignatureValidator.verify(Hex.decode(txid), Hex.decode(signatureValue), Hex.decode(hexAddress));
        System.out.println("签名验证结果:"+verify);
        return false;
    }

}
