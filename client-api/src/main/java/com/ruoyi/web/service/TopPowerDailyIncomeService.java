package com.ruoyi.web.service;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.dto.AccountRequest;
import com.ruoyi.web.entity.TopPowerDailyIncome;
import com.ruoyi.web.enums.Account;
import com.ruoyi.web.mapper.TopPowerDailyIncomeMapper;
import com.ruoyi.web.mapper.TopPowerOrderIncomeMapper;
import com.ruoyi.web.mapper.TopPowerSharingIncomeMapper;
import com.ruoyi.web.vo.OrderIncomeProcessVO;
import com.ruoyi.web.vo.SharingIncomeProcessVO;
import com.ruoyi.web.vo.UserProcessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TopPowerDailyIncomeService extends ServiceImpl<TopPowerDailyIncomeMapper, TopPowerDailyIncome> {

    @Autowired
    private TopPowerOrderIncomeMapper orderIncomeMapper;

    @Autowired
    private TopPowerSharingIncomeMapper sharingIncomeMapper;

    @Autowired
    private TopAccountService accountService;

    /**
     * 收益合账
     */
    @Transactional(rollbackFor = Exception.class)
    public void process(List<UserProcessVO> userVOList, LocalDate processDate) {
        List<AccountRequest> requests = new ArrayList<>();
        for (int i = 0; i < userVOList.size(); i++) {
            UserProcessVO userVO = userVOList.get(i);
            List<OrderIncomeProcessVO> orderIncomeList = orderIncomeMapper.selectProcessIncomeByUserId(userVO.getId());
            List<SharingIncomeProcessVO> sharingIncomeList = sharingIncomeMapper.selectProcessIncomeByUserId(userVO.getId());
            // 用户每日订单收益
            Map<String, List<OrderIncomeProcessVO>> orderIncomeMap = orderIncomeList.stream().collect(Collectors.groupingByConcurrent(OrderIncomeProcessVO::getSymbol));
            orderIncomeMap.forEach((symbol, incomes) -> {
                TopPowerDailyIncome dailyIncome = new TopPowerDailyIncome();
                dailyIncome.setUserId(userVO.getId());
                dailyIncome.setSymbol(symbol);
                dailyIncome.setIncome(incomes.stream().map(OrderIncomeProcessVO::getIncome).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
                dailyIncome.setProcessEnabled(Boolean.FALSE);
                dailyIncome.setProcessDate(processDate);
                dailyIncome.setCreatedDate(LocalDateTime.now());
                dailyIncome.setCreatedBy("SYS");
                dailyIncome.setUpdatedDate(LocalDateTime.now());
                dailyIncome.setUpdatedBy("SYS");
                baseMapper.insert(dailyIncome);
            });
            // 邀请人收益
            for (SharingIncomeProcessVO sharing : sharingIncomeList) {
                requests.add(
                        AccountRequest.builder()
                                .uniqueId(UUID.fastUUID().toString().concat("_" + userVO.getId()).concat("_" + Account.TxType.STORE_IN.typeCode))
                                .userId(userVO.getId())
                                .token(sharing.getSymbol())
                                .fee(BigDecimal.ZERO)
                                .balanceChanged(sharing.getIncome())
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.POWER_SHARING_INCOME)
                                .remark("挖矿邀请收益")
                                .build()
                );
            }
            // 更新
            batchUpdate(userVO.getId());
        }
        if (!requests.isEmpty()) {
            accountService.processAccount(requests);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(Long userId) {
        orderIncomeMapper.updateProcessEnabled(userId);
        sharingIncomeMapper.updateProcessEnabled(userId);
    }

}
