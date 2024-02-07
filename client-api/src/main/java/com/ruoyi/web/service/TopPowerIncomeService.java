package com.ruoyi.web.service;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.dto.AccountRequest;
import com.ruoyi.web.entity.TopPowerIncome;
import com.ruoyi.web.enums.Account;
import com.ruoyi.web.mapper.TopPowerDailyIncomeMapper;
import com.ruoyi.web.mapper.TopPowerIncomeMapper;
import com.ruoyi.web.mapper.TopPowerSharingIncomeMapper;
import com.ruoyi.web.vo.DailyIncomeProcessVO;
import com.ruoyi.web.vo.SharingIncomeProcessVO;
import com.ruoyi.web.vo.UserProcessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TopPowerIncomeService extends ServiceImpl<TopPowerIncomeMapper, TopPowerIncome> {

    @Autowired
    private TopPowerDailyIncomeMapper dailyIncomeMapper;

    @Autowired
    private TopPowerSharingIncomeMapper sharingIncomeMapper;

    @Autowired
    private TopAccountService accountService;

    /**
     * 入账
     */
    @Transactional(rollbackFor = Exception.class)
    public void process(List<UserProcessVO> userVOList, LocalDate processDate) {
        List<AccountRequest> requests = new ArrayList<>();
        for (int i = 0; i < userVOList.size(); i++) {
            UserProcessVO useVO = userVOList.get(i);
            List<DailyIncomeProcessVO> dailyIncomeList = dailyIncomeMapper.selectProcessIncomeByUserId(useVO.getId());
            List<SharingIncomeProcessVO> sharingIncomeList = sharingIncomeMapper.selectProcessIncomeByUserId(useVO.getId());

            for (DailyIncomeProcessVO daily : dailyIncomeList) {
                requests.add(
                        AccountRequest.builder()
                                .uniqueId(UUID.fastUUID().toString().concat("_" + useVO.getId()).concat("_" + Account.TxType.STORE_IN.typeCode))
                                .userId(useVO.getId())
                                .token(daily.getSymbol())
                                .fee(BigDecimal.ZERO)
                                .balanceChanged(daily.getIncome())
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.POWER_DAILY_INCOME)
                                .remark("挖矿天收益")
                                .build()
                );
            }

            for (SharingIncomeProcessVO sharing : sharingIncomeList) {
                requests.add(
                        AccountRequest.builder()
                                .uniqueId(UUID.fastUUID().toString().concat("_" + useVO.getId()).concat("_" + Account.TxType.STORE_IN.typeCode))
                                .userId(useVO.getId())
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
            batchUpdate(useVO.getId());
        }
        if (!requests.isEmpty()) {
            accountService.processAccount(requests);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(Long userId) {
        dailyIncomeMapper.updateProcessEnabled(userId);
        sharingIncomeMapper.updateProcessEnabled(userId);
    }
}