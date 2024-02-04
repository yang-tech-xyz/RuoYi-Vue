package com.ruoyi.web.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.entity.TopChain;
import com.ruoyi.web.entity.TopToken;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.service.TopChainService;
import com.ruoyi.web.service.TopTokenService;
import com.ruoyi.web.service.TopUserService;
import com.ruoyi.web.utils.NumbersUtils;
import com.ruoyi.web.utils.UnsignMessageUtils;
import com.ruoyi.web.vo.RechargeBody;
import com.ruoyi.web.vo.TopTokenChainVO;
import com.ruoyi.web.vo.WalletRegisterBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.abi.TypeDecoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.lang.reflect.Method;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 充值
 * 
 * @author ruoyi
 */
@Slf4j
@RequestMapping("/token")
@Tag(description = "TokenController", name = "token信息")
@RestController
public class CoinController
{
    @Autowired
    private TopChainService topChainService;

    @Autowired
    private TopTokenService topTokenService;

    @Operation(summary = "根据链id查询所有支持的token")
    @GetMapping("queryTokensByChainId")
    public AjaxResult queryTokensByChainId(@Parameter String chainId){
        List<TopTokenChainVO> list= topTokenService.queryTokensByChainId(chainId);
        return AjaxResult.success("success",list);
    }


    /**
     * 充值
     *
     * @param rechargeBody 充值hash值
     * @return 结果
     */
    @Operation(summary = "充值到账")
    @PostMapping("/recharge")
    public AjaxResult recharge(@RequestBody RechargeBody rechargeBody)throws Exception{
        AjaxResult ajax = AjaxResult.success();
        try{
            Integer chainId = rechargeBody.getChainId();
            Optional<TopChain> topChainOpt = topChainService.getOptByChainId(chainId);
            if(!topChainOpt.isPresent()){
                return AjaxResult.error("chain not exist!");
            }
            TopChain topChain = topChainOpt.get();
            String rpcEndpoint = topChain.getRpcEndpoint();
            Web3j web3j = Web3j.build(new HttpService(rpcEndpoint));
            String hash = rechargeBody.getHash();
            //通过hash获取交易
            Response.Error error = web3j.ethGetTransactionByHash(hash).send().getError();
            Optional<TransactionReceipt> transactionReceipt = web3j.ethGetTransactionReceipt(hash).send().getTransactionReceipt();
            if(!transactionReceipt.isPresent()){
                log.warn("get transactionReceipt error! hash value:{}",hash);
                return AjaxResult.error("get transactionReceipt error!");
            }
            log.info("transactionReceipt is:{}",transactionReceipt);
            String status = transactionReceipt.get().getStatus();
            log.info("transactionReceipt status is:{}",status);
            if(!"0x1".equals(status)){
                return AjaxResult.error("transaction not success!");
            }
            Optional<Transaction> transactions = web3j.ethGetTransactionByHash(hash).send().getTransaction();
            if(!transactions.isPresent()){
                return AjaxResult.error("get transaction error!");
            }
            Transaction transaction = transactions.get();

            // 获取币种信息
            Optional<TopToken> topTokenOpt = topTokenService.queryTokenBySymbol(rechargeBody.getTokenSymbol());
            if(!topTokenOpt.isPresent()){
                return AjaxResult.error("token not exist");
            }
            Integer tokenId = topTokenOpt.get().getId();
            Optional<TopTokenChainVO> topTokenChainVOOptional = topTokenService.queryTokenByTokenIdAndChainId(tokenId,topChain.getChainId());
            if(!topTokenChainVOOptional.isPresent()){
                return AjaxResult.error("tokenChain not exist");
            }
            String erc20AddressConfig = topTokenChainVOOptional.get().getErc20Address();
            String erc20Address = transaction.getTo();
            if(!erc20AddressConfig.equalsIgnoreCase(erc20Address)){
                log.warn("erc20 address error! erc20AddressConfig is:{},erc20Address is:{}",erc20AddressConfig,erc20Address);
                return AjaxResult.error("recharge chain error");
            }
            String jsonString = JSONObject.toJSONString(transaction);
            log.info("jsonString is:{}",jsonString);
            String inputData = transaction.getInput();
            log.info("input is:{}",inputData);
            String method = inputData.substring(0,10);
            System.out.println(method);
            String to = inputData.substring(10,74);
            String value = inputData.substring(74);
            Method refMethod = TypeDecoder.class.getDeclaredMethod("decode",String.class,int.class,Class.class);
            refMethod.setAccessible(true);
            Address address = (Address)refMethod.invoke(null,to,0,Address.class);
            System.out.println(address.toString());
            Uint256 amount = (Uint256) refMethod.invoke(null,value,0,Uint256.class);
            System.out.println(amount.getValue());
        }catch (Exception e){
            log.error("system error",e);
            throw e;
        }

        return ajax;
    }


}
