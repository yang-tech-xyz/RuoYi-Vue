package com.ruoyi.web.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.cron.timingwheel.SystemTimer;
import cn.hutool.cron.timingwheel.TimerTask;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.dto.AccountRequest;
import com.ruoyi.web.entity.TopChain;
import com.ruoyi.web.entity.TopToken;
import com.ruoyi.web.entity.TopTransaction;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.enums.Account;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.mapper.TopTokenMapper;
import com.ruoyi.web.vo.RechargeBody;
import com.ruoyi.web.vo.TopTokenChainVO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.TypeDecoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class TopTokenService extends ServiceImpl<TopTokenMapper, TopToken> {

    static SystemTimer systemTimer = new SystemTimer();

    static{
        systemTimer.start();
    }
    @Autowired
    private TopChainService topChainService;

    @Autowired
    private TopTransactionService topTransactionService;

    @Autowired
    private TopAccountService accountService;

    @Autowired
    private TopUserService topUserService;

    @Autowired
    private TopTokenService topTokenService;

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
            Optional<TransactionReceipt> transactionReceiptOptional = web3j.ethGetTransactionReceipt(hash).send().getTransactionReceipt();
            if(!transactionReceiptOptional.isPresent()){
                log.warn("get transactionReceiptOptional error! hash value:{}",hash);
                return AjaxResult.error("get transactionReceiptOptional error!");
            }
            log.info("transactionReceiptOptional is:{}",transactionReceiptOptional);
            // 获取用户信息
            TransactionReceipt transactionReceipt = transactionReceiptOptional.get();
            String from = transactionReceipt.getFrom();
            Optional<TopUserEntity> topUserOptional = topUserService.getByWallet(from);
            if(!topUserOptional.isPresent()){
                log.warn("user not exist,user address is:{}",from);
                throw new ServiceException("user not exist");
            }
            Long userId = topUserOptional.get().getId();
            topTransaction.setUserId(userId);

            String status = transactionReceipt.getStatus();
            topTransaction.setStatus(status);

            log.info("transactionReceiptOptional status is:{}",status);
            if(!"0x1".equals(status)){
                return AjaxResult.error("transaction not success!");
            }
            if(transactionReceipt.getLogs().size()==0){
                throw new ServiceException("transaction have no logs");
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
            log.info("method is:{}",method);
            String to = inputData.substring(10,74);
            String value = inputData.substring(74);
            Method refMethod = TypeDecoder.class.getDeclaredMethod("decode",String.class,int.class,Class.class);
            refMethod.setAccessible(true);
            Address address = (Address)refMethod.invoke(null,to,0,Address.class);
            log.info("address is:{}",address.toString());
            Uint256 amount = (Uint256) refMethod.invoke(null,value,0,Uint256.class);
            BigDecimal pow = new BigDecimal(10).pow(topToken.getDecimals());
            BigDecimal tokenAmount = new BigDecimal(amount.getValue().toString()).divide(pow,10,1);
            topTransaction.setTokenAmount(tokenAmount);
            topTransaction.setHeight(transaction.getBlockNumber());

            log.info("tokenAmount is:{}",tokenAmount);
            topTransaction.setIsConfirm(1);
            topTransaction.setCreateTime(LocalDateTime.now());
            topTransaction.setUpdateTime(LocalDateTime.now());
            topTransaction.setCreateBy(userId.toString());
            topTransaction.setUpdateBy(userId.toString());
            topTransaction.setBlockConfirm(topChain.getBlockConfirm());
            topTransactionService.save(topTransaction);

            systemTimer.addTask(new TimerTask(() -> topTokenService.confirmRechargeToken(hash), 10000));
        }catch (Exception e){
            log.error("system error",e);
            throw e;
        }

        return ajax;
    }

    public Optional<TopTokenChainVO> queryTokenByTokenIdAndChainId(Integer tokenId, Long chainId) {
        return this.baseMapper.queryTokenByTokenIdAndChainId(tokenId,chainId);
    }

    /**
     * 重新加载所有未执行的延时任务
     */
    @PostConstruct
    public void initCountDown() {
        //查询未确认的hash
        List<TopTransaction> topTransactionList = topTransactionService.queryUnConfirm();
        topTransactionList.stream().forEach(t->{
            systemTimer.addTask(new TimerTask(() -> topTokenService.confirmRechargeToken(t.getHash()), 10000));
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean confirmRechargeToken(String hash){
        try{
            Optional<TopTransaction> topTransactionOptional = topTransactionService.getTransactionByHash(hash);
            if(!topTransactionOptional.isPresent()){
                throw new ServiceException("transaction not exist!");
            }
            TopTransaction topTransaction = topTransactionOptional.get();
            // 重复检查transaction是否已经确认交易.
            if(topTransaction.getIsConfirm()==0){
                throw new ServiceException("transaction had been confirmed!");
            }
            String rpcEndpoint = topTransaction.getRpcEndpoint();
            Web3j web3j = Web3j.build(new HttpService(rpcEndpoint));
            //获取当前的区块高度
            EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().sendAsync().get();
            BigInteger currentHeight = ethBlockNumber.getBlockNumber();
            BigInteger topTransactionHeight = topTransaction.getHeight();
            //已经超过确认的区块高度.确认用户充值到账成功.写入用户的账户.
            if(currentHeight.compareTo(topTransactionHeight.add(BigInteger.valueOf(topTransaction.getBlockConfirm())))==1){
                topTransaction.setIsConfirm(0);
                Long mebId = topTransaction.getUserId().longValue();
                accountService.processAccount(
                        Arrays.asList(
                                AccountRequest.builder()
                                        .uniqueId(UUID.fastUUID().toString().concat("_" + mebId).concat("_" + Account.TxType.RECHARGE_IN.typeCode))
                                        .userId(mebId)
                                        .token(topTransaction.getSymbol())
                                        .fee(BigDecimal.ZERO)
                                        .balanceChanged(topTransaction.getTokenAmount())
                                        .balanceTxType(Account.Balance.AVAILABLE)
                                        .txType(Account.TxType.RECHARGE_IN)
                                        .remark("充值")
                                        .build()
                        )
                );
                topTransactionService.updateConfirm(topTransaction);
            }else{
                systemTimer.addTask(new TimerTask(() -> topTokenService.confirmRechargeToken(hash), 10000));
            }
            return true;
        }catch (Exception e){
            log.error("confirmRechargeToken error:",e);
            throw new ServiceException(e);
        }

    }
}
