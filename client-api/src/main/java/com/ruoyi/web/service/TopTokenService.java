package com.ruoyi.web.service;

import cn.hutool.cron.timingwheel.SystemTimer;
import cn.hutool.cron.timingwheel.TimerTask;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.entity.TopChain;
import com.ruoyi.web.entity.TopToken;
import com.ruoyi.web.entity.TopTransaction;
import com.ruoyi.web.mapper.TopTokenMapper;
import com.ruoyi.web.vo.RechargeBody;
import com.ruoyi.web.vo.TopTokenChainVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.TypeDecoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TopTokenService extends ServiceImpl<TopTokenMapper, TopToken> {

    @Autowired
    private TopChainService topChainService;

    @Autowired
    private TopTransactionService topTransactionService;

    public List<TopTokenChainVO> queryTokensByChainId(String chainId) {
        return this.baseMapper.queryTokensByChainId(chainId);
    }

    public Optional<TopToken> queryTokenBySymbol(String tokenSymbol) {
        LambdaQueryWrapper<TopToken> query = Wrappers.lambdaQuery();
        query.eq(TopToken::getSymbol,tokenSymbol);
        return this.getOneOpt(query);
    }

    @Transactional
    public AjaxResult recharge(RechargeBody rechargeBody)throws Exception {
        AjaxResult ajax = AjaxResult.success();
        try{

            TopTransaction topTransaction = new TopTransaction();
            Integer chainId = rechargeBody.getChainId();
            topTransaction.setChainId(chainId);
            Optional<TopChain> topChainOpt = topChainService.getOptByChainId(chainId);
            if(!topChainOpt.isPresent()){
                return AjaxResult.error("chain not exist!");
            }
            TopChain topChain = topChainOpt.get();
            String rpcEndpoint = topChain.getRpcEndpoint();
            topTransaction.setRpcEndpoint(rpcEndpoint);
            Web3j web3j = Web3j.build(new HttpService(rpcEndpoint));
            String hash = rechargeBody.getHash();
            topTransaction.setHash(hash);
            //通过hash获取交易
            Optional<TransactionReceipt> transactionReceipt = web3j.ethGetTransactionReceipt(hash).send().getTransactionReceipt();
            if(!transactionReceipt.isPresent()){
                log.warn("get transactionReceipt error! hash value:{}",hash);
                return AjaxResult.error("get transactionReceipt error!");
            }
            log.info("transactionReceipt is:{}",transactionReceipt);
            String status = transactionReceipt.get().getStatus();
            topTransaction.setStatus(status);
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
            Optional<TopToken> topTokenOpt = this.queryTokenBySymbol(rechargeBody.getTokenSymbol());
            if(!topTokenOpt.isPresent()){
                return AjaxResult.error("token not exist");
            }
            TopToken topToken = topTokenOpt.get();
            Integer tokenId = topToken.getId();
            topTransaction.setTokenId(tokenId);
            topTransaction.setSymbol(topToken.getSymbol());
            Optional<TopTokenChainVO> topTokenChainVOOptional = this.queryTokenByTokenIdAndChainId(tokenId,topChain.getChainId());
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
            log.info("address is:{}",address.toString());
            Uint256 amount = (Uint256) refMethod.invoke(null,value,0,Uint256.class);
            BigDecimal pow = new BigDecimal(10).pow(topToken.getDecimals());
            BigDecimal tokenAmount = new BigDecimal(amount.getValue().toString()).divide(pow,10, RoundingMode.FLOOR);
            topTransaction.setTokenAmount(tokenAmount);
            log.info("tokenAmount is:{}",tokenAmount);
            topTransaction.setConfirm(1);
            topTransactionService.save(topTransaction);
            SystemTimer systemTimer = new SystemTimer();
            systemTimer.start();
            systemTimer.addTask(new TimerTask(() -> log.info("执行延时任务:{}", LocalTime.now()), 5000));
        }catch (Exception e){
            log.error("system error",e);
            throw e;
        }

        return ajax;
    }

    public Optional<TopTokenChainVO> queryTokenByTokenIdAndChainId(Integer tokenId, Long chainId) {
        return this.baseMapper.queryTokenByTokenIdAndChainId(tokenId,chainId);
    }
}
