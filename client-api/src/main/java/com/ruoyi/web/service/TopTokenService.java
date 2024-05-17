package com.ruoyi.web.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.cron.timingwheel.SystemTimer;
import cn.hutool.cron.timingwheel.TimerTask;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.protobuf.ByteString;
import com.ruoyi.web.common.CommonStatus;
import com.ruoyi.web.dto.AccountRequest;
import com.ruoyi.web.entity.*;
import com.ruoyi.web.enums.Account;
import com.ruoyi.web.enums.Plate;
import com.ruoyi.web.enums.TransactionType;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.mapper.TopTokenMapper;
import com.ruoyi.web.vo.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.exceptions.IllegalException;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Response;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class TopTokenService extends ServiceImpl<TopTokenMapper, TopToken> {


    @Value("${spring.profiles.active}")
    private String env;

    static Integer OverCountsRetry = 60;
    public static SystemTimer systemTimer = new SystemTimer();

    static {
        systemTimer.start();
    }

    @Autowired
    private TopTRONService topTRONService;

    @Autowired
    private TopChainService topChainService;

    @Autowired
    private TopTransactionService topTransactionService;

    @Autowired
    private TopAccountService accountService;

    @Autowired
    private TopAccountTxService accountTxService;

    @Autowired
    private TopUserService topUserService;

    @Autowired
    private TopTokenService topTokenService;

    @Autowired
    private TopPowerConfigService topPowerConfigService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${token.secret}")
    private String secret;

    /**
     * 重新加载所有未执行的延时任务
     */
    @PostConstruct
    public void initCountDown() {
        // 查询tron未确认transaction
        List<TopTransaction> topTronRechargeTransactionList = topTransactionService.queryTronRechargeUnConfirm();
        topTronRechargeTransactionList.stream().forEach(t -> {
            systemTimer.addTask(new TimerTask(() -> topTokenService.confirmTronRechargeToken(t.getHash()), 10000));
        });
        //查询未确认的hash
        List<TopTransaction> topRechargeTransactionList = topTransactionService.queryRechargeUnConfirm();
        topRechargeTransactionList.stream().forEach(t -> {
            systemTimer.addTask(new TimerTask(() -> topTokenService.confirmRechargeToken(t.getHash()), 10000));
        });
//        List<TopTransaction> topWithdrawTransactionList = topTransactionService.queryWithdrawUnConfirm();
//        topWithdrawTransactionList.stream().forEach(t -> {
//            systemTimer.addTask(new TimerTask(() -> topTokenService.confirmWithdrawToken(t.getHash()), 10000));
//        });
    }

    public List<TopTokenChainVO> queryTokensByChainId(String chainId) {
        return this.baseMapper.queryTokensByChainId(chainId);
    }

    public Optional<TopToken> queryTokenBySymbol(String tokenSymbol) {
        LambdaQueryWrapper<TopToken> query = Wrappers.lambdaQuery();
        query.eq(TopToken::getSymbol, tokenSymbol);
        return this.getOneOpt(query);
    }

    @Transactional
    public void recharge(RechargeBody rechargeBody) throws Exception {
        log.info("recharge in,rechargeBody is:{}",rechargeBody);
        String hash = rechargeBody.getHash();
        try {
            TopTransaction topTransaction = new TopTransaction();
            Long chainId = rechargeBody.getChainId();
            topTransaction.setChainId(chainId);
            Optional<TopChain> topChainOpt = topChainService.getOptByChainId(chainId);
            if (!topChainOpt.isPresent()) {
                throw new ServiceException("chain not exist!");
            }
            TopChain topChain = topChainOpt.get();
            String rpcEndpoint = topChain.getRpcEndpoint();
            topTransaction.setRpcEndpoint(rpcEndpoint);
            String receiveAddress = topChain.getReceiveAddress();
            Web3j web3j = Web3j.build(new HttpService(rpcEndpoint));
            topTransaction.setHash(hash);
            //通过hash获取交易
            Optional<Transaction> transactionOptional = web3j.ethGetTransactionByHash(hash).send().getTransaction();
            if (!transactionOptional.isPresent()) {
                throw new ServiceException("get transaction error!");
            }
            Transaction transaction = transactionOptional.get();
            String from = transaction.getFrom();

            TopUser topUserEntity = topUserService.getByWallet(from);
            Long userId = topUserEntity.getId();
            topTransaction.setUserId(userId);
            // 设置充值状态为未成功.事务成功状态为0x1
            topTransaction.setStatus(CommonStatus.STATES_COMMIT);

            // 获取币种信息
            Optional<TopToken> topTokenOpt = this.queryTokenBySymbol(rechargeBody.getTokenSymbol());
            if (!topTokenOpt.isPresent()) {
                throw new ServiceException("token not exist");
            }
            TopToken topToken = topTokenOpt.get();
            Integer tokenId = topToken.getId();
            topTransaction.setTokenId(tokenId);
            topTransaction.setSymbol(topToken.getSymbol());
            Optional<TopTokenChainVO> topTokenChainVOOptional = this.queryTokenByTokenIdAndChainId(tokenId, topChain.getChainId());
            if (!topTokenChainVOOptional.isPresent()) {
                throw new ServiceException("tokenChain not exist");
            }
            String erc20AddressConfig = topTokenChainVOOptional.get().getErc20Address();
            String erc20Address = transaction.getTo();
            // 如果该币种不是对应的这个erc20的地址.则该笔充值为伪造的充值.
            if (!erc20AddressConfig.equalsIgnoreCase(erc20Address)) {
                log.error("erc20 address error! erc20AddressConfig is:{},erc20Address is:{}", erc20AddressConfig, erc20Address);
                throw new ServiceException("recharge chain erc20 address not match error");
            }
//            String jsonString = JSONObject.toJSONString(transaction);
            log.info("transaction is:{}", transaction);
            String inputData = transaction.getInput();
            log.info("input is:{}", inputData);
            String method = inputData.substring(0, 10);
            log.info("method is:{}", method);
            String to = inputData.substring(10, 74);
            String value = inputData.substring(74);
            Method refMethod = TypeDecoder.class.getDeclaredMethod("decode", String.class, int.class, Class.class);
            refMethod.setAccessible(true);
            Address address = (Address) refMethod.invoke(null, to, 0, Address.class);
            log.info("address is transfer address???:{}", address.getValue());
            // TODO 检查项目方的地址
            if (!receiveAddress.equalsIgnoreCase(address.getValue())) {
                throw new ServiceException("the address is not the project wallet address");
            }
            Uint256 amount = (Uint256) refMethod.invoke(null, value, 0, Uint256.class);
            BigInteger decimalOfContract = getDecimalOfContract(web3j, erc20AddressConfig, address.getValue());
            // TODO 修改此处的小数位为合约的小数位.
            BigDecimal pow = new BigDecimal(10).pow(decimalOfContract.intValue());
            BigDecimal tokenAmount = new BigDecimal(amount.getValue().toString()).divide(pow, 10, 1);
            topTransaction.setTokenAmount(tokenAmount);
            EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().sendAsync().get();
            BigInteger currentHeight = ethBlockNumber.getBlockNumber();
            topTransaction.setHeight(currentHeight);

            log.info("tokenAmount is:{}", tokenAmount);
            topTransaction.setIsConfirm(0);
            topTransaction.setCreateTime(LocalDateTime.now());
            topTransaction.setUpdateTime(LocalDateTime.now());
            topTransaction.setCreateBy(userId.toString());
            topTransaction.setUpdateBy(userId.toString());
            topTransaction.setBlockConfirm(topChain.getBlockConfirm());
            topTransaction.setType(TransactionType.Recharge);
            topTransactionService.save(topTransaction);
            log.info("recharge end,hash is:{}",rechargeBody.getHash());
        } catch (Exception e) {
            log.error("system error", e);
            throw new ServiceException("recharge error!");
        }
    }
    @Transactional
    public void rechargeTRX(RechargeBody rechargeBody) throws Exception {
        log.info("recharge in,hash is:{}",rechargeBody.getHash());
        ApiWrapper wrapper = null;
        try {
            TopTransaction topTransaction = new TopTransaction();
            Long chainId = rechargeBody.getChainId();
            topTransaction.setChainId(chainId);
            Optional<TopChain> topChainOpt = topChainService.getOptByChainId(chainId);
            if (!topChainOpt.isPresent()) {
                throw new ServiceException("chain not exist!");
            }
            // chainId is -1;
            TopChain topChain = topChainOpt.get();
            String rpcEndpoint = topChain.getRpcEndpoint();
            topTransaction.setRpcEndpoint(rpcEndpoint);
            String usdtReceivedWallet = topChain.getReceiveAddress();
            if(StringUtils.isEmpty(usdtReceivedWallet)){
                throw new ServiceException("usdtReceivedWallet not exist!");
            }

            if("dev".equalsIgnoreCase(env)){
                // 随便给一个私钥即可
                wrapper = ApiWrapper.ofNile("2b34557b528df6d1a0d824c47590e814bcb8269492776634d57902600eb72351");
            }else{
                wrapper = ApiWrapper.ofMainnet("2b34557b528df6d1a0d824c47590e814bcb8269492776634d57902600eb72351","13cba328-e4df-4c14-b5fd-77d9f92df2f7");
            }


            String hash = rechargeBody.getHash();
            topTransaction.setHash(hash);
            //通过hash获取交易
            Response.TransactionInfo transactionInfo = wrapper.getTransactionInfoById(hash);

//            if (!"Success".equalsIgnoreCase(transactionInfo.getResult().toString())) {
//                return AjaxResult.error("get transaction error!");
//            }
            Response.TransactionInfo.Log log1 = transactionInfo.getLog(0);
            ByteString data = log1.getData();

            String from = "41"+Numeric.toHexStringNoPrefixZeroPadded(Numeric.decodeQuantity(Numeric.toHexString(log1.getTopics(1).toByteArray())),40);
            String to = "41"+Numeric.toHexStringNoPrefixZeroPadded(Numeric.decodeQuantity(Numeric.toHexString(log1.getTopics(2).toByteArray())),40);
            BigInteger value = Numeric.decodeQuantity((Numeric.toHexString(data.toByteArray())));
            ByteString contractAddress = transactionInfo.getContractAddress();
            String erc20Address = Numeric.toHexString(contractAddress.toByteArray());

            TopUser topUserEntity = topUserService.getByTronWallet(from);
            Long userId = topUserEntity.getId();
            topTransaction.setUserId(userId);
            // 设置充值状态为未成功.事务成功状态为0x1
            topTransaction.setStatus(CommonStatus.STATES_COMMIT);

            // 获取币种信息
            Optional<TopToken> topTokenOpt = this.queryTokenBySymbol(rechargeBody.getTokenSymbol());
            if (!topTokenOpt.isPresent()) {
                throw new ServiceException("token not exist");
            }
            TopToken topToken = topTokenOpt.get();
            Integer tokenId = topToken.getId();
            topTransaction.setTokenId(tokenId);
            topTransaction.setSymbol(topToken.getSymbol());
            Optional<TopTokenChainVO> topTokenChainVOOptional = this.queryTokenByTokenIdAndChainId(tokenId, topChain.getChainId());
            if (!topTokenChainVOOptional.isPresent()) {
                throw new ServiceException("tokenChain not exist");
            }
            String erc20AddressConfig = topTokenChainVOOptional.get().getErc20Address();

            // 如果该币种不是对应的这个erc20的地址.则该笔充值为伪造的充值.
            if (!("0x"+erc20AddressConfig).equalsIgnoreCase(erc20Address)) {
                log.error("erc20 address error! erc20AddressConfig is:{},erc20Address is:{}", erc20AddressConfig, erc20Address);
                throw new ServiceException("recharge chain erc20 address not match error");
            }
            // TODO 检查项目方的地址
            if (!usdtReceivedWallet.equalsIgnoreCase(to)) {
                throw new ServiceException("the address is not the project wallet address");
            }
            BigInteger decimalOfContract = topTRONService.getTronDecimalOfContract(wrapper, erc20AddressConfig, from);
            // TODO 修改此处的小数位为合约的小数位.
            BigDecimal pow = new BigDecimal(10).pow(decimalOfContract.intValue());
            BigDecimal tokenAmount = new BigDecimal(value.toString()).divide(pow, 10, 1);
            topTransaction.setTokenAmount(tokenAmount);

            long currentBlockNumber = wrapper.getNowBlock().getBlockHeader().getRawData().getNumber();
            topTransaction.setHeight(BigInteger.valueOf(currentBlockNumber));

            log.info("tokenAmount is:{}", tokenAmount);
            topTransaction.setIsConfirm(0);
            topTransaction.setCreateTime(LocalDateTime.now());
            topTransaction.setUpdateTime(LocalDateTime.now());
            topTransaction.setCreateBy(userId.toString());
            topTransaction.setUpdateBy(userId.toString());
            topTransaction.setBlockConfirm(topChain.getBlockConfirm());
            topTransaction.setType(TransactionType.TronRecharge);
            topTransactionService.save(topTransaction);
            log.info("recharge end,hash is:{}",rechargeBody.getHash());
        } catch (Exception e) {
            log.error("system error", e);
            throw new ServiceException("tron recharge error!");
        }finally {
            if(wrapper!=null){
                wrapper.close();
            }
        }
    }

    private BigInteger getDecimalOfContract(Web3j web3j, String contractAddress, String from) throws IOException {
        // Define the function we want to invoke from the smart contract
        Function function = new Function("decimals", Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {
                }));


        // Encode it for the contract to understand
        String encodedFunction = FunctionEncoder.encode(function);


        /*
        Send the request and wait for the response using eth call since
        it's a read only transaction with no cost associated
        */
        EthCall response = web3j.ethCall(
                org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(from, contractAddress, encodedFunction),
                DefaultBlockParameterName.LATEST).send();

        return Numeric.toBigInt(response.getValue());
    }


    private boolean validateTransactionReceipt(String hash, Web3j web3j) throws Exception {
        Optional<TransactionReceipt> transactionReceiptOptional = web3j.ethGetTransactionReceipt(hash).send().getTransactionReceipt();
        if (!transactionReceiptOptional.isPresent()) {
            log.error("get transactionReceiptOptional error! hash value:{}", hash);
            return false;
//            throw new ServiceException("get transactionReceiptOptional error!");
        }
        log.info("transactionReceiptOptional is:{}", transactionReceiptOptional);
        // 获取用户信息
        TransactionReceipt transactionReceipt = transactionReceiptOptional.get();
        String status = transactionReceipt.getStatus();
        if (!"0x1".equals(status)) {
            log.error("transaction not success!");
            return false;
        }
        if (transactionReceipt.getLogs().size() == 0) {
            log.error("transaction have no logs");
            return false;
        }
        return true;
    }
    private boolean validateTronTransactionReceipt(String hash, ApiWrapper wrap,Long chainId) throws Exception {
        Response.TransactionInfo transactionReceiptOptional = wrap.getTransactionInfoById(hash);
        log.info("transactionReceiptOptional is:{}", transactionReceiptOptional);
        // 获取用户信息
        Response.TransactionInfo.code result = transactionReceiptOptional.getResult();
        // FAILED is failed
        if("FAILED".equalsIgnoreCase(result.toString())){
            topTransactionService.updateFailed(hash);
        }
        if(!"SUCESS".equalsIgnoreCase(result.toString())){
            return false;
        }
        long transactionBlockNumber = transactionReceiptOptional.getBlockNumber();
        long currentBlockNumber = wrap.getNowBlock().getBlockHeader().getRawData().getNumber();
        TopChain topChain = topChainService.getOptByChainId(chainId).get();
        Long blockConfirm = topChain.getBlockConfirm();
        if(currentBlockNumber-transactionBlockNumber<blockConfirm){
            return false;
        }
        return true;
    }

    public Optional<TopTokenChainVO> queryTokenByTokenIdAndChainId(Integer tokenId, Long chainId) {
        return this.baseMapper.queryTokenByTokenIdAndChainId(tokenId, chainId);
    }


//    @Transactional(rollbackFor = Exception.class)
//    public boolean confirmRechargeToken(String hash) {
//        try {
//            Optional<TopTransaction> topTransactionOptional = topTransactionService.getTransactionByHash(hash);
//            if (!topTransactionOptional.isPresent()) {
//                throw new ServiceException("transaction not exist!");
//            }
//            TopTransaction topTransaction = topTransactionOptional.get();
//            // 重复检查transaction是否已经确认交易.
//            if (topTransaction.getIsConfirm() == 0) {
//                throw new ServiceException("transaction had been confirmed!");
//            }
//            String rpcEndpoint = topTransaction.getRpcEndpoint();
//            Web3j web3j = Web3j.build(new HttpService(rpcEndpoint));
//            //获取当前的区块高度
//            EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().sendAsync().get();
//            BigInteger currentHeight = ethBlockNumber.getBlockNumber();
//            BigInteger topTransactionHeight = topTransaction.getHeight();
//            //已经超过确认的区块高度.确认用户充值到账成功.写入用户的账户.
//            if (currentHeight.compareTo(topTransactionHeight.add(BigInteger.valueOf(topTransaction.getBlockConfirm()))) == 1) {
//                topTransaction.setIsConfirm(0);
//                Long userId = topTransaction.getUserId().longValue();
//                accountService.processAccount(
//                        Arrays.asList(
//                                AccountRequest.builder()
//                                        .uniqueId(UUID.fastUUID().toString().concat("_" + userId).concat("_" + Account.TxType.RECHARGE_IN.typeCode))
//                                        .userId(userId)
//                                        .token(topTransaction.getSymbol())
//                                        .fee(BigDecimal.ZERO)
//                                        .balanceChanged(topTransaction.getTokenAmount())
//                                        .balanceTxType(Account.Balance.AVAILABLE)
//                                        .txType(Account.TxType.RECHARGE_IN)
//                                        .remark("充值")
//                                        .build()
//                        )
//                );
//                topTransactionService.updateConfirm(topTransaction);
//            } else {
//                systemTimer.addTask(new TimerTask(() -> topTokenService.confirmRechargeToken(hash), 10000));
//            }
//            return true;
//        } catch (Exception e) {
//            log.error("confirmRechargeToken error:", e);
//            throw new ServiceException(e);
//        }
//    }

    @Transactional(rollbackFor = Exception.class)
    public boolean confirmRechargeToken(String hash) {
        try {
            Optional<TopTransaction> topTransactionOptional = topTransactionService.getTransactionByHash(hash);
            if (!topTransactionOptional.isPresent()) {
                throw new ServiceException("transaction not exist!");
            }
            TopTransaction topTransaction = topTransactionOptional.get();

            int retryCounts = topTransaction.getRetryCounts();
            if(retryCounts>=OverCountsRetry){
                return false;
            }
            topTransaction.setRetryCounts(retryCounts+1);
            topTransactionService.updateById(topTransaction);
            // 重复检查transaction是否已经确认交易.已经充值的transaction防止用户重复充值
            if (topTransaction.getIsConfirm() == 1) {
                throw new ServiceException("transaction had been confirmed!");
            }
            String rpcEndpoint = topTransaction.getRpcEndpoint();
            Web3j web3j = Web3j.build(new HttpService(rpcEndpoint));

            //已经超过确认的区块高度.确认用户充值到账成功.写入用户的账户.
            if (validateTransactionReceipt(hash, web3j)) {
                topTransaction.setIsConfirm(1);
                topTransaction.setStatus("0x1");
                Long userId = topTransaction.getUserId().longValue();
                accountService.processAccount(
                        Arrays.asList(
                                AccountRequest.builder()
                                        .uniqueId(hash.concat("_" + userId).concat("_" + Account.TxType.RECHARGE_IN.typeCode))
                                        .userId(userId)
                                        .token(topTransaction.getSymbol())
                                        .fee(BigDecimal.ZERO)
                                        .balanceChanged(topTransaction.getTokenAmount())
                                        .balanceTxType(Account.Balance.AVAILABLE)
                                        .txType(Account.TxType.RECHARGE_IN)
                                        .refNo(hash)
                                        .remark("充值")
                                        .build()
                        )
                );

                topTransactionService.updateConfirm(topTransaction);
            } else {
                systemTimer.addTask(new TimerTask(() -> topTokenService.confirmRechargeToken(hash), 10000));
            }
            return true;
        } catch (Exception e) {
            log.error("confirmRechargeToken error:", e);
            systemTimer.addTask(new TimerTask(() -> topTokenService.confirmRechargeToken(hash), 10000));
            throw new ServiceException(e);
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmTronRechargeToken(String hash) {
        ApiWrapper wrapper = null;
        try {
            Optional<TopTransaction> topTransactionOptional = topTransactionService.getTransactionByHash(hash);
            if (!topTransactionOptional.isPresent()) {
                throw new ServiceException("transaction not exist!");
            }
            TopTransaction topTransaction = topTransactionOptional.get();
            // 重复检查transaction是否已经确认交易.已经充值的transaction防止用户重复充值
            if (topTransaction.getIsConfirm() == 1) {
                throw new ServiceException("transaction had been confirmed!");
            }

            //如果重试次数过大,则不再继续重试
            int retryCounts = topTransaction.getRetryCounts();
            if(retryCounts>=OverCountsRetry){
                return false;
            }
            topTransaction.setRetryCounts(retryCounts+1);
            topTransactionService.updateById(topTransaction);



            if("dev".equalsIgnoreCase(env)){
                wrapper = ApiWrapper.ofNile("2b34557b528df6d1a0d824c47590e814bcb8269492776634d57902600eb72351");
            }else{
                wrapper = ApiWrapper.ofMainnet("2b34557b528df6d1a0d824c47590e814bcb8269492776634d57902600eb72351","13cba328-e4df-4c14-b5fd-77d9f92df2f7");
            }

            //已经超过确认的区块高度.确认用户充值到账成功.写入用户的账户.
            if (validateTronTransactionReceipt(hash, wrapper,topTransaction.getChainId())) {
                topTransaction.setIsConfirm(1);
                topTransaction.setStatus("0x1");
                Long userId = topTransaction.getUserId().longValue();
                accountService.processAccount(
                        Arrays.asList(
                                AccountRequest.builder()
                                        .uniqueId(hash.concat("_" + userId).concat("_" + Account.TxType.TRON_RECHARGE_IN.typeCode))
                                        .userId(userId)
                                        .token(topTransaction.getSymbol())
                                        .fee(BigDecimal.ZERO)
                                        .balanceChanged(topTransaction.getTokenAmount())
                                        .balanceTxType(Account.Balance.AVAILABLE)
                                        .txType(Account.TxType.TRON_RECHARGE_IN)
                                        .refNo(hash)
                                        .remark("波场充值")
                                        .build()
                        )
                );

                topTransactionService.updateConfirm(topTransaction);
            } else {
                systemTimer.addTask(new TimerTask(() -> topTokenService.confirmTronRechargeToken(hash), 10000));
            }
            return true;
        } catch (Exception e) {
            log.error("confirmRechargeToken error:", e);
            systemTimer.addTask(new TimerTask(() -> topTokenService.confirmTronRechargeToken(hash), 10000));
            throw new ServiceException(e);
        }finally {
            if(wrapper!=null){
                wrapper.close();
            }
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public boolean confirmWithdrawToken(String hash) {
        try {
            Optional<TopTransaction> topTransactionOptional = topTransactionService.getTransactionByHash(hash);
            if (!topTransactionOptional.isPresent()) {
                throw new ServiceException("transaction not exist!");
            }
            TopTransaction topTransaction = topTransactionOptional.get();
            // 重复检查transaction是否已经确认交易.已经充值的transaction防止用户重复提现
            if (topTransaction.getIsConfirm() == 1) {
                throw new ServiceException("transaction had been confirmed!");
            }
            String rpcEndpoint = topTransaction.getRpcEndpoint();
            Web3j web3j = Web3j.build(new HttpService(rpcEndpoint));

            //已经超过确认的区块高度.确认用户充值到账成功.写入用户的账户.
            if (validateTransactionReceipt(hash, web3j)) {
                topTransaction.setIsConfirm(1);
                topTransaction.setStatus("0x1");
                Long userId = topTransaction.getUserId().longValue();

                topTransactionService.updateConfirm(topTransaction);
            } else {
                //获取当前的区块高度
                systemTimer.addTask(new TimerTask(() -> topTokenService.confirmWithdrawToken(hash), 10000));
            }
            return true;
        } catch (Exception e) {
            log.error("confirmRechargeToken error:", e);
            systemTimer.addTask(new TimerTask(() -> topTokenService.confirmWithdrawToken(hash), 10000));
            throw new ServiceException(e);
        }
    }

    @Transactional
    public void withdraw(WithdrawBody withdrawBody) throws Exception {
        TopTransaction topTransaction = new TopTransaction();
        //查询用户的账户信息
        String wallet = withdrawBody.getWallet();
        TopUser topUserEntity = topUserService.getByWallet(wallet);
        Long userId = topUserEntity.getId();
        String symbol = withdrawBody.getSymbol();
        TopAccount account = accountService.getAccount(userId, symbol);
        if (account == null) {
            log.error("account not exist,userId is:{},symbol is:{}", userId, symbol);
            throw new ServiceException("account not exist");
        }
        BigDecimal withdrawAmount = withdrawBody.getAmount();
        // 检查账户中的资金是否充足
        if (account.getAvailableBalance().compareTo(withdrawAmount) < 0) {
            log.error("account exceed balance,account balance is:{},symbol is:{}", account.getAvailableBalance(), withdrawAmount);
            throw new ServiceException("account exceed balance");
        }

        // 链上转账
        Optional<TopToken> topTokenOptional = topTokenService.queryTokenBySymbol(symbol);
        if (!topTokenOptional.isPresent()) {
            log.error("token not exist!,symbol is:{}", symbol);
            throw new ServiceException("token not exist!");
        }
        TopToken topToken = topTokenOptional.get();
        Integer topTokenId = topToken.getId();
        Long chainId = withdrawBody.getChainId();
        Optional<TopTokenChainVO> topTokenChainVOOptional = this.queryTokenByTokenIdAndChainId(topTokenId, chainId);
        if (!topTokenChainVOOptional.isPresent()) {
            log.error("token chain config is not exist,tokenId is:{},chainId is:{}", topTokenId, chainId);
            throw new ServiceException("token chain config is not exist");
        }
        TopTokenChainVO topTokenChainVO = topTokenChainVOOptional.get();
        String contractAddress = topTokenChainVO.getErc20Address();
        Optional<TopChain> optByChainIdOptional = topChainService.getOptByChainId(chainId);
        if (!optByChainIdOptional.isPresent()) {
            log.error("chain is not exist,chainId is:{}", chainId);
            throw new ServiceException("chain is not exist");
        }
        TopChain topChain = optByChainIdOptional.get();
        String rpcEndpoint = topChain.getRpcEndpoint();
        String to = wallet; //为了保护资金安全,转账只能转到用户注册的钱包地址
        Web3j web3j = Web3j.build(new HttpService(rpcEndpoint));

        BigInteger decimalOfContract = getDecimalOfContract(web3j, contractAddress, wallet);


        TopPowerConfig topPowerConfig = topPowerConfigService.list().getFirst();
        BigDecimal feeRatio = topPowerConfig.getFeeRatio();
        if (feeRatio == null) {
            throw new ServiceException("fee ratio is null");
        }
        BigDecimal fee = withdrawAmount.multiply(feeRatio);
        // 实际到账金额应该减去手续费
//        BigDecimal transferAmount = withdrawAmount.subtract(fee);
        BigDecimal transferAmount = withdrawAmount.subtract(fee);


        BigInteger tokenAmount = transferAmount.multiply(new BigDecimal("10").pow(decimalOfContract.intValue())).toBigInteger();
        // TODO get the privateKey;
        if (topPowerConfig == null) {
            throw new ServiceException("power config is not exist");
        }

        String uuid = UUID.fastUUID().toString();

        //扣除用户的资金
        accountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(uuid.concat("_" + userId).concat("_" + Account.TxType.WITHDRAW.typeCode))
                                .userId(userId)
                                .token(symbol)
                                .fee(fee.negate())
                                .balanceChanged(transferAmount.negate())
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.WITHDRAW)
                                .refNo(uuid)
                                .remark("提现")
                                .build()
                )
        );
//        topTransaction.setHash(transactionHash);
        topTransaction.setWithdrawReceiveAddress(to);
        topTransaction.setErc20Address(contractAddress);
        topTransaction.setTransNo(uuid);
        topTransaction.setChainId(chainId);
        topTransaction.setTokenId(topTokenId);
        topTransaction.setRpcEndpoint(rpcEndpoint);
        topTransaction.setStatus(CommonStatus.STATES_COMMIT);
        topTransaction.setUserId(userId);
        topTransaction.setSymbol(symbol);
        topTransaction.setTokenAmount(transferAmount);
        topTransaction.setWithdrawAmount(tokenAmount);
        topTransaction.setIsConfirm(CommonStatus.UN_CONFIRM);
        EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().sendAsync().get();
        BigInteger currentHeight = ethBlockNumber.getBlockNumber();
        topTransaction.setHeight(currentHeight);
        topTransaction.setCreateTime(LocalDateTime.now());
        topTransaction.setUpdateTime(LocalDateTime.now());
        topTransaction.setCreateBy(userId.toString());
        topTransaction.setUpdateBy(userId.toString());
        topTransaction.setBlockConfirm(topChain.getBlockConfirm());
        topTransaction.setType(TransactionType.Withdraw);
        topTransactionService.save(topTransaction);
//        systemTimer.addTask(new TimerTask(() -> topTokenService.confirmWithdrawToken(transactionHash), 10000));
    }
    @Transactional
    public void withdrawTron(WithdrawBody withdrawBody) throws Exception {
        ApiWrapper wrapper = null;
        try {
            TopTransaction topTransaction = new TopTransaction();
            //查询用户的账户信息
            String wallet = withdrawBody.getWallet();
            TopUser topUserEntity = topUserService.getByWallet(wallet);
            // 重新设置提币地址为波场钱包地址
            wallet = topUserEntity.getTronWallet();
            if (org.apache.commons.lang3.StringUtils.isBlank(wallet)) {
                throw new ServiceException("tron wallet is empty");
            }
            Long userId = topUserEntity.getId();
            String symbol = withdrawBody.getSymbol();
            TopAccount account = accountService.getAccount(userId, symbol);
            if (account == null) {
                log.error("account not exist,userId is:{},symbol is:{}", userId, symbol);
                throw new ServiceException("account not exist");
            }
            BigDecimal withdrawAmount = withdrawBody.getAmount();
            // 检查账户中的资金是否充足
            if (account.getAvailableBalance().compareTo(withdrawAmount) < 0) {
                log.error("account exceed balance,account balance is:{},symbol is:{}", account.getAvailableBalance(), withdrawAmount);
                throw new ServiceException("account exceed balance");
            }

            // 链上转账
            Optional<TopToken> topTokenOptional = topTokenService.queryTokenBySymbol(symbol);
            if (!topTokenOptional.isPresent()) {
                log.error("token not exist!,symbol is:{}", symbol);
                throw new ServiceException("token not exist!");
            }
            TopToken topToken = topTokenOptional.get();
            Integer topTokenId = topToken.getId();
            Long chainId = withdrawBody.getChainId();
            Optional<TopTokenChainVO> topTokenChainVOOptional = this.queryTokenByTokenIdAndChainId(topTokenId, chainId);
            if (!topTokenChainVOOptional.isPresent()) {
                log.error("token chain config is not exist,tokenId is:{},chainId is:{}", topTokenId, chainId);
                throw new ServiceException("token chain config is not exist");
            }
            TopPowerConfig topPowerConfig = topPowerConfigService.list().getFirst();
            if (topPowerConfig == null) {
                throw new ServiceException("power config is not exist");
            }

            TopTokenChainVO topTokenChainVO = topTokenChainVOOptional.get();
            String contractAddress = topTokenChainVO.getErc20Address();
            Optional<TopChain> optByChainIdOptional = topChainService.getOptByChainId(chainId);
            if (!optByChainIdOptional.isPresent()) {
                log.error("chain is not exist,chainId is:{}", chainId);
                throw new ServiceException("chain is not exist");
            }
            TopChain topChain = optByChainIdOptional.get();
            String rpcEndpoint = topChain.getRpcEndpoint();
            String to = wallet; //为了保护资金安全,转账只能转到用户注册的钱包地址
//        Web3j web3j = Web3j.build(new HttpService(rpcEndpoint));

            if ("dev".equalsIgnoreCase(env)) {
                // 随便给一个私钥即可
                wrapper = ApiWrapper.ofNile("2b34557b528df6d1a0d824c47590e814bcb8269492776634d57902600eb72351");
            } else {
                wrapper = ApiWrapper.ofMainnet("2b34557b528df6d1a0d824c47590e814bcb8269492776634d57902600eb72351", "13cba328-e4df-4c14-b5fd-77d9f92df2f7");
            }
            BigInteger tronDecimalOfContract = topTRONService.getTronDecimalOfContract(wrapper, contractAddress, wallet);
//        BigInteger decimalOfContract = getDecimalOfContract(web3j, contractAddress, wallet);


            BigDecimal feeRatio = topPowerConfig.getFeeRatio();
            if (feeRatio == null) {
                throw new ServiceException("fee ratio is null");
            }
            BigDecimal fee = withdrawAmount.multiply(feeRatio);
            // 实际到账金额应该减去手续费
//        BigDecimal transferAmount = withdrawAmount.subtract(fee);
            BigDecimal transferAmount = withdrawAmount.subtract(fee);


            BigInteger tokenAmount = transferAmount.multiply(new BigDecimal("10").pow(tronDecimalOfContract.intValue())).toBigInteger();
            // TODO get the privateKey;

//        String curve = topPowerConfig.getCurve();
//        String iv = "1234567812345678";
//        String key = secret.substring(0, 16);
//        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());
//        String s = aes.decryptStr(curve);
//        String transactionHash = transferToken(web3j, contractAddress, s, to, tokenAmount);

            String uuid = UUID.fastUUID().toString();

            //扣除用户的资金
            accountService.processAccount(
                    Arrays.asList(
                            AccountRequest.builder()
                                    .uniqueId(uuid.concat("_" + userId).concat("_" + Account.TxType.TRON_WITHDRAW.typeCode))
                                    .userId(userId)
                                    .token(symbol)
                                    .fee(fee.negate())
                                    .balanceChanged(transferAmount.negate())
                                    .balanceTxType(Account.Balance.AVAILABLE)
                                    .txType(Account.TxType.TRON_WITHDRAW)
                                    .refNo(uuid)
                                    .remark("波场提现")
                                    .build()
                    )
            );
//        topTransaction.setHash(transactionHash);
            topTransaction.setWithdrawReceiveAddress(to);
            topTransaction.setErc20Address(contractAddress);
            topTransaction.setTransNo(uuid);
            topTransaction.setChainId(chainId);
            topTransaction.setTokenId(topTokenId);
            topTransaction.setRpcEndpoint(rpcEndpoint);
            topTransaction.setStatus(CommonStatus.STATES_COMMIT);
            topTransaction.setUserId(userId);
            topTransaction.setSymbol(symbol);
            topTransaction.setTokenAmount(transferAmount);
            topTransaction.setWithdrawAmount(tokenAmount);
            topTransaction.setIsConfirm(CommonStatus.UN_CONFIRM);
            long number = wrapper.getNowBlock().getBlockHeader().getRawData().getNumber();
            BigInteger currentHeight = BigInteger.valueOf(number);
            topTransaction.setHeight(currentHeight);
            topTransaction.setCreateTime(LocalDateTime.now());
            topTransaction.setUpdateTime(LocalDateTime.now());
            topTransaction.setCreateBy(userId.toString());
            topTransaction.setUpdateBy(userId.toString());
            topTransaction.setBlockConfirm(topChain.getBlockConfirm());
            topTransaction.setType(TransactionType.Tron_Withdraw);
            topTransactionService.save(topTransaction);
        }catch (Exception e){
            log.error("withdraw error!",e);
            throw new ServiceException("withdraw tron error"+e.getMessage());
        }finally {
            if(wrapper!=null){
                wrapper.close();
            }
        }

//        systemTimer.addTask(new TimerTask(() -> topTokenService.confirmWithdrawToken(transactionHash), 10000));
    }

    /**
     * btc 提现
     *
     * @param withdrawBody
     * @return
     * @throws Exception
     */
    @Transactional
    public void withdrawBTC(WithdrawBody withdrawBody) throws Exception {
        TopTransaction topTransaction = new TopTransaction();
        //查询用户的账户信息
        String wallet = withdrawBody.getWallet();
        TopUser topUserEntity = topUserService.getByWallet(wallet);
        String btcTransferAddress = topUserEntity.getBtcTransferAddress();
        if (StringUtils.isEmpty(btcTransferAddress)) {
            throw new ServiceException("btcWallet is empty!");
        }
        Long userId = topUserEntity.getId();
        String symbol = withdrawBody.getSymbol();
        TopAccount account = accountService.getAccount(userId, symbol);
        if (account == null) {
            log.error("account not exist,userId is:{},symbol is:{}", userId, symbol);
            throw new ServiceException("account not exist");
        }

        Optional<TopToken> topTokenOptional = topTokenService.queryTokenBySymbol(symbol);
        if (!topTokenOptional.isPresent()) {
            log.error("token not exist!,symbol is:{}", symbol);
            throw new ServiceException("token not exist!");
        }
        TopToken topToken = topTokenOptional.get();
        Integer topTokenId = topToken.getId();

        BigDecimal withdrawAmount = withdrawBody.getAmount();
        TopPowerConfig topPowerConfig = topPowerConfigService.list().getFirst();
        BigDecimal feeRatio = topPowerConfig.getFeeRatio();
        if (feeRatio == null) {
            throw new ServiceException("fee ratio is null");
        }
        BigDecimal fee = withdrawAmount.multiply(feeRatio);
        // 实际到账金额应该减去手续费
//        BigDecimal transferAmount = withdrawAmount.subtract(fee);
        BigDecimal transferAmount = withdrawAmount.subtract(fee);
        // 检查账户中的资金是否充足
        if (account.getAvailableBalance().compareTo(withdrawAmount) < 0) {
            log.error("account exceed balance,account balance is:{},symbol is:{}", account.getAvailableBalance(), withdrawAmount);
            throw new ServiceException("account exceed balance");
        }

        String uuid = UUID.fastUUID().toString();
        //扣除用户的资金
        accountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(uuid.concat("_" + userId).concat("_" + Account.TxType.WITHDRAW_BTC.typeCode))
                                .userId(userId)
                                .token(symbol)
                                .fee(fee.negate())
                                .balanceChanged(transferAmount.negate())
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.WITHDRAW_BTC)
                                .refNo(uuid)
                                .remark("提现")
                                .build()
                )
        );
//        topTransaction.setHash(transactionHash);
        topTransaction.setWithdrawReceiveAddress(btcTransferAddress);
        topTransaction.setTransNo(uuid);
        topTransaction.setChainId(0L);
        topTransaction.setTokenId(topTokenId);
        topTransaction.setRpcEndpoint("");
        topTransaction.setStatus(CommonStatus.STATES_COMMIT);
        topTransaction.setUserId(userId);
        topTransaction.setSymbol(symbol);
        topTransaction.setTokenAmount(transferAmount);
        topTransaction.setIsConfirm(CommonStatus.UN_CONFIRM);
//        topTransaction.setHeight(currentHeight);
        topTransaction.setCreateTime(LocalDateTime.now());
        topTransaction.setUpdateTime(LocalDateTime.now());
        topTransaction.setCreateBy(userId.toString());
        topTransaction.setUpdateBy(userId.toString());
//        topTransaction.setBlockConfirm(topChain.getBlockConfirm());
        topTransaction.setType(TransactionType.Withdraw_BTC);
        topTransactionService.save(topTransaction);
    }

    public String transferToken(Web3j web3j, String contractAddress, String privateKey, String to, BigInteger amount) throws Exception {

        BigInteger bigInteger = new BigInteger(privateKey, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(bigInteger);
        Credentials credentials = Credentials.create(ecKeyPair);
        String fromAddress = credentials.getAddress();
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        Address address = new Address(to);
        Uint256 value = new Uint256(amount);
        List<Type> parametersList = new ArrayList<>();
        parametersList.add(address);
        parametersList.add(value);
        List<TypeReference<?>> outList = new ArrayList<>();
        Function function = new Function("transfer", parametersList, outList);
        String encodedFunction = FunctionEncoder.encode(function);
        log.info("gas price is:{}", DefaultGasProvider.GAS_PRICE);
        log.info("DefaultGasProvider.GAS_LIMIT is:{}", DefaultGasProvider.GAS_LIMIT);
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, DefaultGasProvider.GAS_PRICE,
                new BigInteger("210000"), contractAddress, encodedFunction);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        Object transactionHash = ethSendTransaction.getTransactionHash();
        log.info("transactionHash is:{}", transactionHash.toString());
        return transactionHash.toString();
    }

    @Transactional
    public void internalTransferBody(InternalTransferBody internalTransferBody) {
        String receiveAddress = internalTransferBody.getReceiveAddress();
        TopUser receiveUserEntity = topUserService.getByWallet(receiveAddress);
        Long receiveUserId = receiveUserEntity.getId();
        String sendWallet = internalTransferBody.getWallet();
        TopUser sendUser = topUserService.getByWallet(sendWallet);
        Long sendUserId = sendUser.getId();
        String symbol = internalTransferBody.getSymbol();
        BigDecimal amount = internalTransferBody.getAmount();

        // 从发出用户转出资金
        UUID uuid = UUID.fastUUID();
        accountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(uuid.toString().concat("_" + sendUserId).concat("_" + Account.TxType.INTERNAL_TRANSFER.typeCode))
                                .userId(sendUserId)
                                .token(symbol)
                                .fee(BigDecimal.ZERO)
                                .balanceChanged(amount.negate())
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.INTERNAL_TRANSFER)
                                .refNo(uuid.toString())
                                .remark("转出")
                                .build()
                )
        );
        // 转入
        accountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(uuid.toString().concat("_" + receiveUserId).concat("_" + Account.TxType.INTERNAL_TRANSFER.typeCode))
                                .userId(receiveUserId)
                                .token(symbol)
                                .fee(BigDecimal.ZERO)
                                .balanceChanged(amount)
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.INTERNAL_TRANSFER)
                                .refNo(uuid.toString())
                                .remark("转入")
                                .build()
                )
        );
    }

    @Transactional
    public void exchangeBTC2USDT(ExchangeBody exchangeBody) {
        BigDecimal amount = exchangeBody.getAmount();
        String wallet = exchangeBody.getWallet();
        Long userId = topUserService.getByWallet(wallet).getId();
        String originSymbol = "BTC";
        String exchangeSymbol = "USDT";

        BigDecimal btcPrice = getPrice(originSymbol);
        BigDecimal exchangeAmount = amount.multiply(btcPrice);


        // 扣除BTC资金
        UUID uuid = UUID.fastUUID();
        accountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(uuid.toString().concat("_" + userId).concat("_" + Account.TxType.EXCHANGE.typeCode))
                                .userId(userId)
                                .token(originSymbol)
                                .fee(BigDecimal.ZERO)
                                .balanceChanged(amount.negate())
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.EXCHANGE)
                                .refNo(uuid.toString())
                                .remark("转出")
                                .build()
                )
        );
        // 转换成USDT资金
        accountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(UUID.fastUUID().toString().concat("_" + userId).concat("_" + Account.TxType.EXCHANGE.typeCode))
                                .userId(userId)
                                .token(exchangeSymbol)
                                .fee(BigDecimal.ZERO)
                                .balanceChanged(exchangeAmount)
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.EXCHANGE)
                                .refNo(uuid.toString())
                                .remark("转入")
                                .build()
                )
        );
    }

    public List<TokenVO> getList() {
        return baseMapper.selectOnlineListVO();
    }

    public TopToken getBySymbol(String symbol) {
        return baseMapper.selectOne(new LambdaQueryWrapper<TopToken>().eq(TopToken::getSymbol, symbol));
    }

    @Transactional
    public void exchangeUsdt2BTCF(ExchangeBody exchangeBody) {
        BigDecimal amount = exchangeBody.getAmount();
        String wallet = exchangeBody.getWallet();
        Long userId = topUserService.getByWallet(wallet).getId();
        String originSymbol = "USDT";
        String exchangeSymbol = "BTCF";

        BigDecimal btcPrice = getPrice(exchangeSymbol);
        BigDecimal exchangeAmount = amount.divide(btcPrice, 10, 2);
        //检查当日兑换的BTCF是否超过100万个。
        // 检查当日的兑换量，是否达到了100w
        LocalDate now = LocalDate.now();
        BigDecimal btcfSumExchangeAmount = accountTxService.sumExchangeAmount(exchangeSymbol, now,now.plusDays(1));
        if(btcfSumExchangeAmount.compareTo(new BigDecimal("1000000")) > 0){
            throw new ServiceException("Over restrict amount");
        }

        // 扣除USDT资金
        UUID uuid = UUID.fastUUID();
        accountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(uuid.toString().concat("_" + userId).concat("_" + Account.TxType.EXCHANGE.typeCode))
                                .userId(userId)
                                .token(originSymbol)
                                .fee(BigDecimal.ZERO)
                                .balanceChanged(amount.negate())
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.EXCHANGE)
                                .refNo(uuid.toString())
                                .remark("转出")
                                .build()
                )
        );
        // 转换成BTCF资金
        accountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(UUID.fastUUID().toString().concat("_" + userId).concat("_" + Account.TxType.EXCHANGE.typeCode))
                                .userId(userId)
                                .token(exchangeSymbol)
                                .fee(BigDecimal.ZERO)
                                .balanceChanged(exchangeAmount)
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.EXCHANGE)
                                .refNo(uuid.toString())
                                .remark("转入")
                                .build()
                )
        );
    }


    @Transactional
    public void exchangeBTC2BTCF(ExchangeBody exchangeBody) {
        BigDecimal amount = exchangeBody.getAmount();
        String wallet = exchangeBody.getWallet();
        Long userId = topUserService.getByWallet(wallet).getId();
        String originSymbol = "BTC";
        String exchangeSymbol = "BTCF";

        BigDecimal btcPrice = getPrice(originSymbol);
        BigDecimal btcfPrice = getPrice(exchangeSymbol);
        BigDecimal exchangeAmount = amount.multiply(btcPrice).divide(btcfPrice, 10, 2);
        // 检查当日的兑换量，是否达到了100w
        LocalDate now = LocalDate.now();
        BigDecimal btcfSumExchangeAmount = accountTxService.sumExchangeAmount(exchangeSymbol, now,now.plusDays(1));
        if(btcfSumExchangeAmount.compareTo(new BigDecimal("1000000")) > 0){
            throw new ServiceException("Over restrict amount");
        }
        // 扣除USDT资金
        UUID uuid = UUID.fastUUID();
        accountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(uuid.toString().concat("_" + userId).concat("_" + Account.TxType.EXCHANGE.typeCode))
                                .userId(userId)
                                .token(originSymbol)
                                .fee(BigDecimal.ZERO)
                                .balanceChanged(amount.negate())
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.EXCHANGE)
                                .refNo(uuid.toString())
                                .remark("转出")
                                .build()
                )
        );
        // 转换成BTCF资金
        accountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(UUID.fastUUID().toString().concat("_" + userId).concat("_" + Account.TxType.EXCHANGE.typeCode))
                                .userId(userId)
                                .token(exchangeSymbol)
                                .fee(BigDecimal.ZERO)
                                .balanceChanged(exchangeAmount)
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.EXCHANGE)
                                .refNo(uuid.toString())
                                .remark("转入")
                                .build()
                )
        );
    }

    public BigDecimal getPrice(String symbol) {
        Optional<TopToken> optional = Optional.ofNullable(baseMapper.selectOne(new LambdaQueryWrapper<TopToken>()
                .eq(TopToken::getSymbol, symbol)));
        if (optional.isPresent()) {
            BigDecimal price = optional.get().getPrice();
            if (price.compareTo(BigDecimal.ZERO) == 0) {
                throw new ServiceException("无法获取价格", 500);
            }
            return price;
        }
        throw new ServiceException("无法获取价格", 500);
    }

    /**
     * 刷新币种价格
     * 1.对接不同平台价格
     */
    public void refPrice() {
        List<TopToken> tokens = baseMapper.selectList(new LambdaQueryWrapper<TopToken>()
                .eq(TopToken::getAutoPriceEnabled, Boolean.TRUE));
        for (TopToken token : tokens) {
            try {
                BigDecimal price = BigDecimal.ONE;
                try {
                    if (Plate.GATE_IO._code.equals(token.getPlate())) {
                        String result = restTemplate.getForObject(Plate.GATE_IO._url + "/api/v4/spot/tickers?currency_pair=" + token.getSymbol() + "_" + "USDT", String.class);
                        List<TickerVO> tickerVOS = JSONArray.parseArray(result, TickerVO.class);
                        price = tickerVOS.get(0).getLast();
                    } else if (Plate.BINANCE._code.equals(token.getPlate())) {
                        String result = restTemplate.getForObject(Plate.BINANCE._url + "/api/v3/ticker/price?symbol=" + token.getSymbol() + "USDT", String.class);
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        price = jsonObject.getBigDecimal("price");
                    } else if (Plate.OKX._code.equals(token.getPlate())) {
                        String result = restTemplate.getForObject(Plate.OKX._url + "/api/v5/market/ticker?instId=" + token.getSymbol() + "_" + "USDT-SWAP", String.class);
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        price = jsonObject.getJSONArray("data").getJSONObject(0).getBigDecimal("last");
                    } else if (Plate.HUO_BI._code.equals(token.getPlate())) {
                        String result = restTemplate.getForObject(Plate.HUO_BI._url + "/market/detail/merged?symbol=" + token.getSymbol().toLowerCase() + "usdt", String.class);
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        price = jsonObject.getJSONObject("tick").getBigDecimal("close");
                    }
                } catch (Exception ignored) {
                }
                token.setPrice(price);
                token.setUpdateBy("SYS");
                token.setUpdateTime(LocalDateTime.now());
                baseMapper.updateById(token);
            } catch (Exception ex) {
                log.error("token price error:{}", token.getSymbol(), ex);
            }
        }
    }

    public Map<String, TopToken> getTopToken() {
        Map<String, TopToken> tokenMap = new HashMap<>();
        List<TopToken> tokens = baseMapper.selectList(new LambdaQueryWrapper<>());
        for (TopToken token : tokens) {
            tokenMap.put(token.getSymbol(), token);
        }
        return tokenMap;
    }

    public static void main(String[] args) {
        try {
            Web3j web3j = Web3j.build(new HttpService("https://bsc-dataseed3.ninicoin.io"));
            Optional<Transaction> transactionOptional = web3j.ethGetTransactionByHash("0xcf83c42425062c691ffed7280045b916470b9ea96a5c1aed5fd42eafec50f5a9").send().getTransaction();
            if (!transactionOptional.isPresent()) {
                throw new ServiceException("get transaction error!");
            }
            System.out.println(transactionOptional.isPresent());
        }catch (Exception e){
            System.out.println(e);
        }
//        {
//            ApiWrapper wrapper = ApiWrapper.ofMainnet("2b34557b528df6d1a0d824c47590e814bcb8269492776634d57902600eb72351", "13cba328-e4df-4c14-b5fd-77d9f92df2f7");
//            try {
//                Chain.Block nowBlock = wrapper.getNowBlock();
//                System.out.println(nowBlock.getBlockHeader().getRawData().getNumber());
//            } catch (IllegalException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        {
//            ApiWrapper wrapper = ApiWrapper.ofMainnet("2b34557b528df6d1a0d824c47590e814bcb8269492776634d57902600eb72351", "13cba328-e4df-4c14-b5fd-77d9f92df2f7");
//            try {
//                Chain.Block nowBlock = wrapper.getNowBlock();
//                System.out.println(nowBlock.getBlockHeader().getRawData().getNumber());
//                wrapper.close();
//            } catch (IllegalException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
}
