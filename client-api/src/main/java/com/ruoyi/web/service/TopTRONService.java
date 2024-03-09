package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.protobuf.ByteString;
import com.ruoyi.web.entity.TopUser;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.mapper.TopUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tron.trident.abi.TypeDecoder;
import org.tron.trident.abi.TypeReference;
import org.tron.trident.abi.datatypes.Address;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.key.KeyPair;
import org.tron.trident.crypto.SECP256K1;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Response;
import org.tron.trident.utils.Base58Check;
import org.tron.trident.utils.Numeric;
import org.tron.trident.utils.Strings;

@Slf4j
@Service
public class TopTRONService extends ServiceImpl<TopUserMapper, TopUser> {
    public void queryTransactionInfoByHash(ApiWrapper wrapper, String hash){
        try{
            long number = wrapper.getNowBlock().getBlockHeader().getRawData().getNumber();
            log.info("nowBlock is:{}",number);
            Response.TransactionInfo transactionInfoById = wrapper.getTransactionInfoById(hash);
            log.info("transactionInfoById is:{}",transactionInfoById);
            ByteString contractAddress = transactionInfoById.getContractAddress();
            Response.TransactionInfo.Log log1 = transactionInfoById.getLog(0);
            ByteString data = log1.getData();
            String myContractAddress = "TBLfSzQo8TGtCotPD5JZntpZfQqPFLehTE";

            Base58Check.base58ToBytes(myContractAddress);

            log.info("contractAddress is:{}",Numeric.toHexString(Base58Check.base58ToBytes(myContractAddress)));
            log.info("contractAddress is:{}",Numeric.toHexString(contractAddress.toByteArray()));
            log.info("log1 is:{}", Numeric.toHexStringNoPrefixZeroPadded(Numeric.decodeQuantity(Numeric.toHexString(log1.getTopics(1).toByteArray())),40));
//            log.info("decode from is:{}", TypeDecoder.decodeAddress("0x41"+Numeric.toHexStringNoPrefixZeroPadded(Numeric.decodeQuantity(Numeric.toHexString(log1.getTopics(1).toByteArray())),40)));
            log.info("log2 is:{}",Numeric.toHexStringNoPrefixZeroPadded(Numeric.decodeQuantity( Numeric.toHexString(log1.getTopics(2).toByteArray())),40));
            log.info("data is:{}", Numeric.decodeQuantity((Numeric.toHexString(data.toByteArray()))));


            long blockNumber = transactionInfoById.getBlockNumber();
            Chain.Transaction.Result.contractResult result = transactionInfoById.getReceipt().getResult();
            log.info("blockNumber is:{}",blockNumber);
            log.info("result is:{}",result);
        }catch (Exception e){
            log.error("query tron transaction error",e);
            throw new ServiceException(e.getMessage());
        }finally {
            wrapper.close();
        }
    }

    /**
     * 字节数组转16进制
     * @param bytes 需要转换的byte数组
     * @return  转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
