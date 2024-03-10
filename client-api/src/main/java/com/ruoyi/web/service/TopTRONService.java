package com.ruoyi.web.service;

import com.google.protobuf.ByteString;
import com.ruoyi.web.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tron.trident.abi.TypeReference;
import org.tron.trident.abi.datatypes.Function;
import org.tron.trident.abi.datatypes.generated.Uint256;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Response;
import org.tron.trident.utils.Base58Check;
import org.tron.trident.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

@Slf4j
@Service
public class TopTRONService {
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

    public BigInteger getTronDecimalOfContract(ApiWrapper wrapper, String contractAddress, String from) throws IOException {
        // Define the function we want to invoke from the smart contract
        Function function = new Function("decimals", Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {
                }));


        /*
        Send the request and wait for the response using eth call since
        it's a read only transaction with no cost associated
        */
        Response.TransactionExtention extension = wrapper.constantCall(from,
                contractAddress,
                function);

        return BigInteger.valueOf(Long.parseLong(Numeric.toHexString(extension.getConstantResult(0).toByteArray())));
    }
}
