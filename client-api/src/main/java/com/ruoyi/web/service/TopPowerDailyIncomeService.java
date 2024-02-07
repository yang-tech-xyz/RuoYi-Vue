package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopPowerDailyIncome;
import com.ruoyi.web.entity.TopPowerOrder;
import com.ruoyi.web.mapper.TopPowerDailyIncomeMapper;
import com.ruoyi.web.vo.TokenPriceVO;
import com.ruoyi.web.vo.UserProcessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TopPowerDailyIncomeService extends ServiceImpl<TopPowerDailyIncomeMapper, TopPowerDailyIncome> {

    /**
     * 用户挖矿天收益
     * 1.按照订单每笔计算
     * 2.每天每单：amount * outPutRatio / period
     * 3.将收益换算为BTC发放
     */
    @Transactional(rollbackFor = Exception.class)
    public void process(List<UserProcessVO> userVOList, List<TokenPriceVO> priceVOList, LocalDate processDate) {
        for (int i = 0; i < userVOList.size(); i++) {
            UserProcessVO userVO = userVOList.get(i);
            for (int j = 0; j < userVO.getPowerOrders().size(); j++) {
                TopPowerOrder order = userVO.getPowerOrders().get(j);
                TopPowerDailyIncome dailyIncome = new TopPowerDailyIncome();
                dailyIncome.setUserId(userVO.getId());
                dailyIncome.setOrderNo(order.getOrderNo());
                dailyIncome.setAmount(order.getAmount());
                dailyIncome.setIncomeSymbol(order.getOutputSymbol());
                dailyIncome.setIncomePrice(priceVOList.stream()
                        .filter(price -> price.getSymbol().equals(order.getOutputSymbol()))
                        .map(TokenPriceVO::getPrice).findFirst().orElse(BigDecimal.ZERO));
                dailyIncome.setIncomeRate(order.getOutputRatio());
                dailyIncome.setIncomeUsd(order.getAmount().multiply(order.getOutputRatio()).divide(BigDecimal.valueOf(order.getPeriod()), 8, RoundingMode.DOWN));
                dailyIncome.setIncome(dailyIncome.getIncomeUsd().divide(dailyIncome.getIncomePrice(), 8, RoundingMode.DOWN));
                dailyIncome.setIncomeDate(processDate);
                dailyIncome.setProcessEnabled(Boolean.FALSE);
                dailyIncome.setCreatedDate(LocalDateTime.now());
                dailyIncome.setCreatedBy("SYS");
                dailyIncome.setUpdatedDate(LocalDateTime.now());
                dailyIncome.setUpdatedBy("SYS");
                baseMapper.insert(dailyIncome);
                userVO.getDailyIncomeMap().put(dailyIncome.getIncomeSymbol(), userVO.getDailyIncomeMap().getOrDefault(dailyIncome.getIncomeSymbol(), BigDecimal.ZERO).add(dailyIncome.getIncome()));
            }
        }
    }
}
