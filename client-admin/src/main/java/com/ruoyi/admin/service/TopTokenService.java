package com.ruoyi.admin.service;

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
import org.web3j.abi.FunctionEncoder;
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
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TopTokenService extends ServiceImpl<TopTokenMapper, TopToken> {


    static SystemTimer systemTimer = new SystemTimer();

    static {
        systemTimer.start();
    }

    @Value("${token.secret}")
    private String secret;
    @Autowired
    private TopPowerConfigService topPowerConfigService;

    @Autowired
    private TopTransactionService topTransactionService;

    @Autowired
    private TopTokenService topTokenService;

    @Autowired
    private TopAccountService topAccountService;


    /**
     * 重新加载所有未执行的延时任务
     */
    @PostConstruct
    public void initCountDown() {
        //查询未确认的hash
//        List<TopTransaction> topRechargeTransactionList = topTransactionService.queryRechargeUnConfirm();
//        topRechargeTransactionList.stream().forEach(t -> {
//            systemTimer.addTask(new TimerTask(() -> topTokenService.confirmRechargeToken(t.getHash()), 10000));
//        });
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
        Web3j web3j = Web3j.build(new HttpService(rpcEndpoint));

        TopPowerConfig topPowerConfig = topPowerConfigService.list().getFirst();


        BigInteger tokenAmount = topTransaction.getWithdrawAmount();
        if (topPowerConfig == null) {
            throw new ServiceException("power config is not exist");
        }
        String curve = topPowerConfig.getCurve();
        String iv = "1234567812345678";
        String key = secret.substring(0, 16);
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());
        String s = aes.decryptStr(curve);
        String transactionHash = transferToken(web3j, contractAddress, s, to, tokenAmount);
        TopTransaction topTransactionEntity = new TopTransaction();
        topTransactionEntity.setId(topTransaction.getId());
        topTransactionEntity.setHash(transactionHash);

        topTransactionService.updateById(topTransactionEntity);
        systemTimer.addTask(new TimerTask(() -> topTokenService.confirmWithdrawToken(transactionHash), 10000));
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

    public String transferToken(Web3j web3j, String contractAddress, String privateKey, String to, BigInteger amount) throws ServiceException {
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
            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, DefaultGasProvider.GAS_PRICE,
                    new BigInteger("210000"), contractAddress, encodedFunction);
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            Object transactionHash = ethSendTransaction.getTransactionHash();
            log.info("transactionHash is:{}", transactionHash.toString());
            return transactionHash.toString();
        }catch (Exception e){
            log.error("transfer error!",e);
            throw new ServiceException("transfer error");
        }
    }

    /**
     * 标记为审核失败并退款
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

}
