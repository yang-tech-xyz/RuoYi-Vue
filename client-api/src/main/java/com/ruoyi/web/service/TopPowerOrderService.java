package com.ruoyi.web.service;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.dto.AccountRequest;
import com.ruoyi.web.entity.TopAccount;
import com.ruoyi.web.entity.TopPowerConfig;
import com.ruoyi.web.entity.TopPowerOrder;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.enums.Account;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.mapper.TopPowerConfigMapper;
import com.ruoyi.web.mapper.TopPowerOrderMapper;
import com.ruoyi.web.vo.BuyPowerBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TopPowerOrderService extends ServiceImpl<TopPowerOrderMapper, TopPowerOrder> {

    @Autowired
    private TopPowerConfigService topPowerConfigService;
    
    @Autowired
    private TopTokenPriceService topTokenPriceService;

    @Autowired
    private TopUserService topUserService;

    @Autowired
    private TopAccountService topAccountService;


    @Transactional
    public void buyOrder(BuyPowerBody buyPowerBody) {
        TopPowerOrder topPowerOrder = new TopPowerOrder();
        List<TopPowerConfig> powerConfigs = topPowerConfigService.list();
        if(powerConfigs==null||powerConfigs.size()==0){
            throw new ServiceException("powerConfig is error!");
        }
        TopPowerConfig topPowerConfig = powerConfigs.get(0);
        //通过购买数量,计算需要的金额.
        BigDecimal price = topPowerConfig.getPrice();
        BigDecimal buyNumbers = buyPowerBody.getNumber();
        topPowerOrder.setNumber(buyNumbers);
        // 购买矿机需要的U的数量
        BigDecimal buyPowerNeedPayUsdt = buyNumbers.multiply(price);
        topPowerOrder.setAmount(buyPowerNeedPayUsdt);
        topPowerOrder.setOutputSymbol(topPowerConfig.getOutputSymbol());
        topPowerOrder.setPeriod(topPowerConfig.getPeriod());
        topPowerOrder.setOutputRatio(topPowerConfig.getOutputRatio());
        topPowerOrder.setEndTime(LocalDateTime.now().plusDays(topPowerOrder.getPeriod()+1));
        String symbol = buyPowerBody.getSymbol();
        // 查询symbol的价格.
        BigDecimal tokenPrice = topTokenPriceService.getPrice(symbol);
        // 计算用户需要的token的数量
        BigDecimal payTokenAmount = buyPowerNeedPayUsdt.divide(tokenPrice, 10, RoundingMode.CEILING);
        String wallet = buyPowerBody.getWallet().toLowerCase();
        Optional<TopUserEntity> topUserOptional = topUserService.getByWallet(wallet);
        if(!topUserOptional.isPresent()){
            log.warn("user not exist,wallet is:{}",wallet);
            throw new ServiceException("user not exist,wallet");
        }
        TopUserEntity topUserEntity = topUserOptional.get();
        TopAccount account = topAccountService.getAccount(topUserEntity.getId().longValue(), symbol);
        if(account.getAvailableBalance().compareTo(payTokenAmount)<0){
            log.warn("account have no enough money,account info:{}",account);
        }
        String orderNo = UUID.fastUUID().toString();
        topPowerOrder.setOrderNo(orderNo);
        Long mebId = account.getUserId();
        // 扣减用户购买算力的钱
        topAccountService.processAccount(
            Arrays.asList(
                AccountRequest.builder()
                    .uniqueId(orderNo.concat("_" + mebId).concat("_" + Account.TxType.PURCHASE.typeCode))
                    .userId(mebId)
                    .token(symbol)
                    .fee(BigDecimal.ZERO)
                    .balanceChanged(payTokenAmount.negate())
                    .balanceTxType(Account.Balance.AVAILABLE)
                    .txType(Account.TxType.PURCHASE)
                    .remark("支付")
                    .build()
            )
                // TODO 是否需要記錄用戶系統賬戶入賬?
        );

        //生成算力訂單
        this.baseMapper.insert(topPowerOrder);
    }
}
