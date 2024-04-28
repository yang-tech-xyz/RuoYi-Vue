package com.ruoyi.admin.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.cron.timingwheel.SystemTimer;
import cn.hutool.cron.timingwheel.TimerTask;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.common.CommonStatus;
import com.ruoyi.admin.dto.TokenAddDTO;
import com.ruoyi.admin.dto.TokenUpdateDTO;
import com.ruoyi.admin.entity.TopChain;
import com.ruoyi.admin.entity.TopPowerConfig;
import com.ruoyi.admin.entity.TopToken;
import com.ruoyi.admin.entity.TopTransaction;
import com.ruoyi.admin.exception.ServiceException;
import com.ruoyi.admin.mapper.TopTokenMapper;
import com.ruoyi.admin.vo.TokenVO;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.contract.Contract;
import org.tron.trident.core.contract.Trc20Contract;
import org.tron.trident.core.key.KeyPair;
import org.tron.trident.proto.Response;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
public class TopTokenService extends ServiceImpl<TopTokenMapper, TopToken> {

    @Value("${spring.profiles.active}")
    private String env;
    static SystemTimer systemTimer = new SystemTimer();

    static {
        systemTimer.start();
    }

    @Value("${token.secret}")
    private String secret;

    @Autowired
    private TopChainService topChainService;

    @Autowired
    private TopPowerConfigService topPowerConfigService;

    @Autowired
    private TopTransactionService topTransactionService;

    @Autowired
    private TopTokenService topTokenService;

    @Autowired
    private TopAccountService topAccountService;

    @Autowired
    private TopTRONService topTRONService;


    /**
     * 重新加载所有未执行的延时任务
     */
    @PostConstruct
    public void initCountDown() {
        //查询未确认的hash
        List<TopTransaction> topTronWithdrawTransactionList = topTransactionService.queryTronWithdrawUnConfirm();
        topTronWithdrawTransactionList.stream().filter(t -> StringUtils.isNotBlank(t.getHash())).forEach(t -> {
            systemTimer.addTask(new TimerTask(() -> topTokenService.confirmTronWithdrawToken(t.getHash()), 10000));
        });
        List<TopTransaction> topWithdrawTransactionList = topTransactionService.queryWithdrawUnConfirm();
        topWithdrawTransactionList.stream().filter(t -> StringUtils.isNotBlank(t.getHash())).forEach(t -> {
            systemTimer.addTask(new TimerTask(() -> topTokenService.confirmWithdrawToken(t.getHash()), 10000));
        });
    }

    public List<TokenVO> getList() {
        return baseMapper.selectListVO();
    }

    public Boolean add(TokenAddDTO dto) {
        TopToken token = new TopToken();
        BeanUtils.copyProperties(dto, token);
        token.setCreateBy("SYS");
        token.setCreateTime(LocalDateTime.now());
        token.setUpdateBy("SYS");
        token.setUpdateTime(LocalDateTime.now());
        baseMapper.insert(token);
        return true;
    }

    public Boolean edit(TokenUpdateDTO dto) {
        TopToken token = baseMapper.selectOne(new LambdaQueryWrapper<TopToken>().eq(TopToken::getSymbol, dto.getSymbol()));
        BeanUtils.copyProperties(dto, token);
        token.setUpdateBy("SYS");
        token.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(token);
        return true;
    }


    public void withdrawAuditPass(TopTransaction topTransaction) {
        String contractAddress = topTransaction.getErc20Address();
        String rpcEndpoint = topTransaction.getRpcEndpoint();
        String to = topTransaction.getWithdrawReceiveAddress(); //为了保护资金安全,转账只能转到用户注册的钱包地址
        Long chainId = topTransaction.getChainId();
        Web3j web3j = Web3j.build(new HttpService(rpcEndpoint));

        TopPowerConfig topPowerConfig = topPowerConfigService.list().getFirst();


        BigInteger tokenAmount = topTransaction.getWithdrawAmount();
        if (topPowerConfig == null) {
            throw new ServiceException("power config is not exist");
        }
        String curve = topPowerConfig.getCurve();
//        String iv = "1234567812345678";
//        String key = secret.substring(0, 16);
//        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());

        // 检查提现账户是否有足够的金额
        boolean amountCheckResult = topTokenService.checkTransferValueEnough(curve, web3j, contractAddress, tokenAmount);
        if (!amountCheckResult) {
            throw new ServiceException("amountCheckResult failed");
        }

        String transactionHash = transferToken(chainId, web3j, contractAddress, curve, to, tokenAmount);
        TopTransaction topTransactionEntity = new TopTransaction();
        topTransactionEntity.setId(topTransaction.getId());
        topTransactionEntity.setHash(transactionHash);

        topTransactionService.updateById(topTransactionEntity);
        systemTimer.addTask(new TimerTask(() -> topTokenService.confirmWithdrawToken(transactionHash), 10000));
    }

    public void tronWithdrawAuditPass(TopTransaction topTransaction) {
        ApiWrapper wrapper = null;
        try {
            String contractAddress = topTransaction.getErc20Address();
//        String rpcEndpoint = topTransaction.getRpcEndpoint();
            String to = topTransaction.getWithdrawReceiveAddress(); //为了保护资金安全,转账只能转到用户注册的钱包地址
            Long chainId = topTransaction.getChainId();
//        Web3j web3j = Web3j.build(new HttpService(rpcEndpoint));
            TopPowerConfig topPowerConfig = topPowerConfigService.list().getFirst();
            if (topPowerConfig == null) {
                throw new ServiceException("power config is not exist");
            }
            String tronCurve = topPowerConfig.getTronCurve();
            log.info("tronCurve is:{}",tronCurve);
            KeyPair keyPair = new KeyPair(tronCurve);
            String from = keyPair.toHexAddress();
            log.info("env is:{}",env);
            if ("dev".equalsIgnoreCase(env)) {
                // 随便给一个私钥即可
                wrapper = ApiWrapper.ofNile(tronCurve);
            } else {
                wrapper = ApiWrapper.ofMainnet(tronCurve, "13cba328-e4df-4c14-b5fd-77d9f92df2f7");
            }


            BigInteger withdrawAmount = topTransaction.getWithdrawAmount();
            BigDecimal tokenAmount = topTransaction.getTokenAmount();


            // 检查提现账户是否有足够的金额
            boolean amountCheckResult = topTokenService.checkTronTransferValueEnough(wrapper, contractAddress, withdrawAmount, from);
            if (!amountCheckResult) {
                throw new ServiceException("amountCheckResult failed");
            }
            BigInteger tronDecimalOfContract = topTRONService.getTronDecimalOfContract(wrapper, contractAddress, from);
            String transactionHash = transferTronToken(wrapper, contractAddress, keyPair, to, tokenAmount.longValue(), tronDecimalOfContract.intValue());
            TopTransaction topTransactionEntity = new TopTransaction();
            topTransactionEntity.setId(topTransaction.getId());
            topTransactionEntity.setHash(transactionHash);

            topTransactionService.updateById(topTransactionEntity);
            systemTimer.addTask(new TimerTask(() -> topTokenService.confirmTronWithdrawToken(transactionHash), 10000));

        } catch (Exception e) {
            log.error("withdraw tron usdt failed", e);
            throw new ServiceException("withdraw tron usdt failed");
        }finally {
            if(wrapper!=null){
                wrapper.close();
            }
        }
    }

    public void withdrawBTCAuditPass(TopTransaction topTransaction) {
        TopTransaction topTransactionEntity = new TopTransaction();
        topTransactionEntity.setId(topTransaction.getId());
        topTransactionEntity.setHash(topTransaction.getHash());
        topTransactionEntity.setStatus(CommonStatus.STATES_SUCCESS);
        topTransactionEntity.setIsConfirm(CommonStatus.IS_CONFIRM);
        topTransactionService.updateById(topTransactionEntity);
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


    private boolean validateTronTransactionReceipt(String hash, ApiWrapper wrap, Long chainId) throws Exception {
        Response.TransactionInfo transactionReceiptOptional = wrap.getTransactionInfoById(hash);
        log.info("transactionReceiptOptional is:{}", transactionReceiptOptional);
        // 获取用户信息
        Response.TransactionInfo.code result = transactionReceiptOptional.getResult();
        // FAILED is failed
        if ("FAILED".equalsIgnoreCase(result.toString())) {
            topTransactionService.updateFailed(hash);
        }
        if (!"SUCESS".equalsIgnoreCase(result.toString())) {
            return false;
        }
        long transactionBlockNumber = transactionReceiptOptional.getBlockNumber();
        long currentBlockNumber = wrap.getNowBlock().getBlockHeader().getRawData().getNumber();
        TopChain topChain = topChainService.getOptByChainId(chainId).get();
        Long blockConfirm = topChain.getBlockConfirm();
        if (currentBlockNumber - transactionBlockNumber < blockConfirm) {
            return false;
        }
        return true;
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
                TopTransaction topTransactionEntity = new TopTransaction();
                topTransactionEntity.setId(topTransaction.getId());
                topTransactionEntity.setIsConfirm(CommonStatus.IS_CONFIRM);
                topTransactionEntity.setStatus(CommonStatus.STATES_SUCCESS);
                Long userId = topTransaction.getUserId().longValue();
                topTransactionService.updateById(topTransactionEntity);
//                topTransactionService.updateConfirm(topTransaction);
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

    @Transactional(rollbackFor = Exception.class)
    public boolean confirmTronWithdrawToken(String hash) {
        ApiWrapper wrapper = null;
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


            if ("dev".equalsIgnoreCase(env)) {
                wrapper = ApiWrapper.ofNile("2b34557b528df6d1a0d824c47590e814bcb8269492776634d57902600eb72351");
            } else {
                wrapper = ApiWrapper.ofMainnet("2b34557b528df6d1a0d824c47590e814bcb8269492776634d57902600eb72351", "13cba328-e4df-4c14-b5fd-77d9f92df2f7");
            }

            //已经超过确认的区块高度.确认用户充值到账成功.写入用户的账户.
            if (validateTronTransactionReceipt(hash, wrapper, topTransaction.getChainId())) {
                TopTransaction topTransactionEntity = new TopTransaction();
                topTransactionEntity.setId(topTransaction.getId());
                topTransactionEntity.setIsConfirm(CommonStatus.IS_CONFIRM);
                topTransactionEntity.setStatus(CommonStatus.STATES_SUCCESS);
                Long userId = topTransaction.getUserId().longValue();
                topTransactionService.updateById(topTransactionEntity);
//                topTransactionService.updateConfirm(topTransaction);
            } else {
                //获取当前的区块高度
                systemTimer.addTask(new TimerTask(() -> topTokenService.confirmTronWithdrawToken(hash), 10000));
            }
            return true;
        } catch (Exception e) {
            log.error("confirmRechargeToken error:", e);
            systemTimer.addTask(new TimerTask(() -> topTokenService.confirmTronWithdrawToken(hash), 10000));
            throw new ServiceException(e);
        }finally {
            if(wrapper!=null){
                wrapper.close();
            }
        }
    }

    public String transferToken(Long chainId, Web3j web3j, String contractAddress, String privateKey, String to, BigInteger amount) throws ServiceException {
        try {
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

//            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, String data
//            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, DefaultGasProvider.GAS_PRICE,
//                    new BigInteger("210000"), contractAddress, encodedFunction);

//            long chainId,BigInteger nonce,BigInteger gasLimit,String to,BigInteger value,String data,BigInteger maxPriorityFeePerGas,BigInteger maxFeePerGas
//            long chainId,
//            BigInteger nonce,
//            BigInteger gasPrice,
//            BigInteger gasLimit,
//            String to,
//            BigInteger value,
//            String data,
//            List<AccessListObject> accessList
            RawTransaction rawTransaction = RawTransaction.createTransaction(chainId, nonce, DefaultGasProvider.GAS_PRICE, new BigInteger("210000"), contractAddress,
                    new BigInteger("0"), encodedFunction, new ArrayList<>());
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            if (ethSendTransaction.hasError()) {
                throw new ServiceException(ethSendTransaction.getError().getMessage());
            }
            Object transactionHash = ethSendTransaction.getTransactionHash();
            log.info("transactionHash is:{}", transactionHash.toString());
            return transactionHash.toString();
        } catch (Exception e) {
            log.error("transfer error!", e);
            throw new ServiceException("transfer error");
        }
    }

    public String transferTronToken(ApiWrapper wrapper, String contractAddress, KeyPair keyPair, String to, Long amount, Integer power) throws ServiceException {
        try {
            log.info("contractAddress is:{}",contractAddress);
            log.info("to is:{}",to);
            log.info("amount is:{}",amount);
            log.info("power is:{}",power);
            long random = RandomUtil.randomLong(System.currentTimeMillis());
            Contract contract = wrapper.getContract(contractAddress);
            Trc20Contract token = new Trc20Contract(contract, keyPair.toHexAddress(), wrapper);
            return token.transfer(to, amount, power, Long.toString(random), 100000000L);
        } catch (Exception e) {
            log.error("transfer error!", e);
            throw new ServiceException("transfer error");
        }
    }

    /**
     * 标记为审核失败并退款
     *
     * @param topTransaction
     */
    @Transactional
    public void withdrawAuditReject(TopTransaction topTransaction) {
        topTransaction.setStatus(CommonStatus.STATES_REJECT);
        topTransaction.setIsConfirm(CommonStatus.REJECT);
        topTransactionService.updateById(topTransaction);
        // 开始退款
        topAccountService.refund(topTransaction.getTransNo());
    }

    public boolean checkTransferValueEnough(String privateKey, Web3j web3j, String contractAddress, BigInteger transferAmount) {
        try {
            BigInteger bigInteger = new BigInteger(privateKey, 16);
            ECKeyPair ecKeyPair = ECKeyPair.create(bigInteger);
            Credentials credentials = Credentials.create(ecKeyPair);
            String fromAddress = credentials.getAddress();
            List<Type> parametersList = new ArrayList<>();
            parametersList.add(new Address(fromAddress));
            List<TypeReference<?>> outList = new ArrayList<>();
            outList.add(TypeReference.create(Uint256.class));
            Function function = new Function(
                    "balanceOf",
                    parametersList,  // Solidity Types in smart contract functions
                    outList);

            String encodedFunction = FunctionEncoder.encode(function);
            org.web3j.protocol.core.methods.response.EthCall response = null;

            response = web3j.ethCall(
                            Transaction.createEthCallTransaction(fromAddress, contractAddress, encodedFunction),
                            DefaultBlockParameterName.LATEST)
                    .sendAsync().get();

            List<Type> balanceOf = FunctionReturnDecoder.decode(
                    response.getValue(), function.getOutputParameters());
            BigInteger balance = (BigInteger) balanceOf.getFirst().getValue();
            return balance.compareTo(transferAmount) > 0;
        } catch (Exception e) {
            log.error("check balance of amount error!", e);
            throw new ServiceException(e.getMessage());
        }
    }

    public boolean checkTronTransferValueEnough(ApiWrapper wrapper, String contractAddress, BigInteger withdrawAmount, String from) {
        try {
            BigInteger tronBalanceOfContract = topTRONService.getTronBalanceOfContract(wrapper, contractAddress, from);
            return tronBalanceOfContract.compareTo(withdrawAmount) > 0;
        } catch (Exception e) {
            log.error("check balance of amount error!", e);
            throw new ServiceException(e.getMessage());
        }
    }

    public static void main(String[] args) {
//        BigInteger bigInteger = new BigInteger("0x58eef556f93ba37d103e48867fd69e4b477f19e6df2d485d89aee0e0d3c3cbec", 16);
//        System.out.println(bigInteger);

        String tronCurve = "84f481672cea847d18d4bce4c159efa2da374403bc2033ecf00cda7831173e98";
        ApiWrapper wrapper = ApiWrapper.ofMainnet(tronCurve, "13cba328-e4df-4c14-b5fd-77d9f92df2f7");
        KeyPair keyPair = new KeyPair(tronCurve);
        String from = keyPair.toHexAddress();

        try {
            Contract contract = wrapper.getContract("41a614f803b6fd780986a42c78ec9c7f77e6ded13c");
            Trc20Contract token = new Trc20Contract(contract, keyPair.toHexAddress(), wrapper);
            long random = RandomUtil.randomLong(System.currentTimeMillis());
            String hash = token.transfer("TUAaiz5WQCRwwQiHV1G6ZheAXETtBMcZQF", 1, 6, Long.toString(random), 100000000L);
            System.out.println("hash is:"+hash);
        } catch (Exception e) {
            log.error("transfer error!", e);
            throw new ServiceException("transfer error");
        }



    }

}
