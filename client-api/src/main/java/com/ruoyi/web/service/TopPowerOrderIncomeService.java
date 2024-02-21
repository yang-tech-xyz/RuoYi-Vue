package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopPowerOrder;
import com.ruoyi.web.entity.TopPowerOrderIncome;
import com.ruoyi.web.entity.TopToken;
import com.ruoyi.web.mapper.TopPowerOrderIncomeMapper;
import com.ruoyi.web.vo.UserProcessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TopPowerOrderIncomeService extends ServiceImpl<TopPowerOrderIncomeMapper, TopPowerOrderIncome> {

    /**
     * 用户挖矿天收益
     * 1.按照订单每笔计算
     * 2.每天每单：投资金额 * （年化利率/产出周期）
     * 3.将收益换算为BTC发放
     */
    @Transactional(rollbackFor = Exception.class)
    public void process(List<UserProcessVO> userVOList, Map<String, TopToken> tokens, LocalDate processDate) {
        for (int i = 0; i < userVOList.size(); i++) {
            UserProcessVO userVO = userVOList.get(i);
            for (int j = 0; j < userVO.getPowerOrders().size(); j++) {
                TopPowerOrder order = userVO.getPowerOrders().get(j);
                TopPowerOrderIncome orderIncome = new TopPowerOrderIncome();
                orderIncome.setUserId(userVO.getId());
                orderIncome.setOrderNo(order.getOrderNo());
                orderIncome.setSymbol(order.getOutputSymbol());
                // 日利率
                orderIncome.setRate(tokens.get(order.getSymbol()).getAnnualInterestRate().divide(BigDecimal.valueOf(order.getPeriod()), 8, RoundingMode.DOWN));
                // 日收益USD：最大8位
                orderIncome.setIncome(order.getAmount().multiply(orderIncome.getRate()));
                // BTC收益:最大10位
                orderIncome.setPrice(tokens.get(orderIncome.getSymbol()).getPrice());
                orderIncome.setIncome(orderIncome.getIncome().divide(orderIncome.getPrice(), 10, RoundingMode.DOWN));
                orderIncome.setProcessDate(processDate);
                orderIncome.setCreatedDate(LocalDateTime.now());
                orderIncome.setCreatedBy("SYS");
                orderIncome.setUpdatedDate(LocalDateTime.now());
                orderIncome.setUpdatedBy("SYS");
                baseMapper.insert(orderIncome);
            }
        }
    }
}
