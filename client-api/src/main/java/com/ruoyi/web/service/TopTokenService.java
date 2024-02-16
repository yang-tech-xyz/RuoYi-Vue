package com.ruoyi.web.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.cron.timingwheel.SystemTimer;
import cn.hutool.cron.timingwheel.TimerTask;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.dto.AccountRequest;
import com.ruoyi.web.entity.*;
import com.ruoyi.web.enums.Account;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TopTokenService extends ServiceImpl<TopTokenMapper, TopToken> {

    static SystemTimer systemTimer = new SystemTimer();

    static {
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

    @Autowired
    private TopPowerConfigService topPowerConfigService;

    @Autowired
    private TopTokenPriceService topTokenPriceService;

    @Value("${token.secret}")
    private String secret;

    public List<TopTokenChainVO> queryTokensByChainId(String chainId) {
        return this.baseMapper.queryTokensByChainId(chainId);
    }

    public Optional<TopToken> queryTokenBySymbol(String tokenSymbol) {
        LambdaQueryWrapper<TopToken> query = Wrappers.lambdaQuery();
        query.eq(TopToken::getSymbol, tokenSymbol);
        return this.getOneOpt(query);
    }

    @Transactional
    public AjaxResult recharge(RechargeBody rechargeBody) throws Exception {
        AjaxResult ajax = AjaxResult.success();
        try {

            TopTransaction topTransaction = new TopTransaction();
            Long chainId = rechargeBody.getChainId();
            topTransaction.setChainId(chainId);
            Optional<TopChain> topChainOpt = topChainService.getOptByChainId(chainId);
            if (!topChainOpt.isPresent()) {
                return AjaxResult.error("chain not exist!");
            }
            TopChain topChain = topChainOpt.get();
            String rpcEndpoint = topChain.getRpcEndpoint();
            topTransaction.setRpcEndpoint(rpcEndpoint);
            String receiveAddress = topChain.getReceiveAddress();
            Web3j web3j = Web3j.build(new HttpService(rpcEndpoint));
            String hash = rechargeBody.getHash();
            topTransaction.setHash(hash);
            //通过hash获取交易
            Optional<Transaction> transactionOptional = web3j.ethGetTransactionByHash(hash).send().getTransaction();
            if (!transactionOptional.isPresent()) {
                return AjaxResult.error("get transaction error!");
            }
            Transaction transaction = transactionOptional.get();
            String from = transaction.getFrom();

            TopUser topUserEntity = topUserService.getByWallet(from);
            Long userId = topUserEntity.getId();
            topTransaction.setUserId(userId);
            // 设置充值状态为未成功.事务成功状态为0x1
            topTransaction.setStatus("0x0");

            // 获取币种信息
            Optional<TopToken> topTokenOpt = this.queryTokenBySymbol(rechargeBody.getTokenSymbol());
            if (!topTokenOpt.isPresent()) {
                return AjaxResult.error("token not exist");
            }
            TopToken topToken = topTokenOpt.get();
            Integer tokenId = topToken.getId();
            topTransaction.setTokenId(tokenId);
            topTransaction.setSymbol(topToken.getSymbol());
            Optional<TopTokenChainVO> topTokenChainVOOptional = this.queryTokenByTokenIdAndChainId(tokenId, topChain.getChainId());
            if (!topTokenChainVOOptional.isPresent()) {
                return AjaxResult.error("tokenChain not exist");
            }
            String erc20AddressConfig = topTokenChainVOOptional.get().getErc20Address();
            String erc20Address = transaction.getTo();
            // 如果该币种不是对应的这个erc20的地址.则该笔充值为伪造的充值.
            if (!erc20AddressConfig.equalsIgnoreCase(erc20Address)) {
                log.error("erc20 address error! erc20AddressConfig is:{},erc20Address is:{}", erc20AddressConfig, erc20Address);
                return AjaxResult.error("recharge chain erc20 address not match error");
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
                return AjaxResult.error("the address is not the project wallet address");
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

            systemTimer.addTask(new TimerTask(() -> topTokenService.confirmRechargeToken(hash), 10000));
        } catch (Exception e) {
            log.error("system error", e);
            throw e;
        }

        return ajax;
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

    public Optional<TopTokenChainVO> queryTokenByTokenIdAndChainId(Integer tokenId, Long chainId) {
        return this.baseMapper.queryTokenByTokenIdAndChainId(tokenId, chainId);
    }

    /**
     * 重新加载所有未执行的延时任务
     */
    @PostConstruct
    public void initCountDown() {
        //查询未确认的hash
        List<TopTransaction> topRechargeTransactionList = topTransactionService.queryRechargeUnConfirm();
        topRechargeTransactionList.stream().forEach(t -> {
            systemTimer.addTask(new TimerTask(() -> topTokenService.confirmRechargeToken(t.getHash()), 10000));
        });
        List<TopTransaction> topWithdrawTransactionList = topTransactionService.queryWithdrawUnConfirm();
        topWithdrawTransactionList.stream().forEach(t -> {
            systemTimer.addTask(new TimerTask(() -> topTokenService.confirmWithdrawToken(t.getHash()), 10000));
        });
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
    public AjaxResult withdraw(WithdrawBody withdrawBody) throws Exception {
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
        String curve = topPowerConfig.getCurve();
        String iv = "1234567812345678";
        String key = secret.substring(0, 16);
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());
        String s = aes.decryptStr(curve);
        String transactionHash = transferToken(web3j, contractAddress, s, to, tokenAmount);

        //扣除用户的资金
        accountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(transactionHash.toString().concat("_" + userId).concat("_" + Account.TxType.WITHDRAW.typeCode))
                                .userId(userId)
                                .token(symbol)
                                .fee(fee.negate())
                                .balanceChanged(transferAmount.negate())
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.WITHDRAW)
                                .refNo(transactionHash.toString())
                                .remark("提现")
                                .build()
                )
        );
        topTransaction.setHash(transactionHash);
        topTransaction.setChainId(chainId);
        topTransaction.setTokenId(topTokenId);
        topTransaction.setRpcEndpoint(rpcEndpoint);
        topTransaction.setStatus("0x1");
        topTransaction.setUserId(userId);
        topTransaction.setSymbol(symbol);
        topTransaction.setTokenAmount(withdrawAmount);
        topTransaction.setIsConfirm(0);
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
        systemTimer.addTask(new TimerTask(() -> topTokenService.confirmWithdrawToken(transactionHash), 10000));
        return AjaxResult.success("success");
    }

    /**
     * btc 提现
     *
     * @param withdrawBody
     * @return
     * @throws Exception
     */
    @Transactional
    public AjaxResult withdrawBTC(WithdrawBody withdrawBody) throws Exception {
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


        //扣除用户的资金
        accountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(UUID.fastUUID().toString().concat("_" + userId).concat("_" + Account.TxType.WITHDRAW_BTC.typeCode))
                                .userId(userId)
                                .token(symbol)
                                .fee(fee.negate())
                                .balanceChanged(transferAmount.negate())
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.WITHDRAW_BTC)
                                .remark("提现")
                                .build()
                )
        );
//        topTransaction.setHash(transactionHash);
        topTransaction.setChainId(0L);
        topTransaction.setTokenId(topTokenId);
        topTransaction.setRpcEndpoint("");
        topTransaction.setStatus("0x1");
        topTransaction.setUserId(userId);
        topTransaction.setSymbol(symbol);
        topTransaction.setTokenAmount(transferAmount);
        topTransaction.setIsConfirm(1);
//        topTransaction.setHeight(currentHeight);
        topTransaction.setCreateTime(LocalDateTime.now());
        topTransaction.setUpdateTime(LocalDateTime.now());
        topTransaction.setCreateBy(userId.toString());
        topTransaction.setUpdateBy(userId.toString());
//        topTransaction.setBlockConfirm(topChain.getBlockConfirm());
        topTransaction.setType(TransactionType.Withdraw_BTC);
        topTransactionService.save(topTransaction);
        return AjaxResult.success("success");
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

    public void exchangeBTC2USDT(ExchangeBody exchangeBody) {
        BigDecimal amount = exchangeBody.getAmount();
        String wallet = exchangeBody.getWallet();
        Long userId = topUserService.getByWallet(wallet).getId();
        String originSymbol = "BTC";
        String exchangeSymbol = "USDT";

        BigDecimal btcPrice = topTokenPriceService.getPrice(originSymbol);
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
        return baseMapper.selectListVO();
    }

    public TopToken getBySymbol(String symbol) {
        return baseMapper.selectOne(new LambdaQueryWrapper<TopToken>().eq(TopToken::getSymbol, symbol));
    }
}
