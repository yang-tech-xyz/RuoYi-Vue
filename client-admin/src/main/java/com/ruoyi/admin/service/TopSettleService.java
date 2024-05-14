package com.ruoyi.admin.service;

import com.ruoyi.admin.mapper.TopSettleMapper;
import com.ruoyi.admin.vo.SettleDepositWithdrawVO;
import com.ruoyi.admin.vo.SettleMemberCountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TopSettleService {

    @Autowired
    private TopSettleMapper settleMapper;

    public SettleMemberCountVO getMemberCount() {
        LocalDate today = LocalDate.now();
        LocalDateTime _start = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime _end = LocalDateTime.of(today, LocalTime.MAX);

        LocalDate yesterday = today.minusDays(1);
        LocalDateTime _yesterday_start = LocalDateTime.of(yesterday, LocalTime.MIN);
        LocalDateTime _yesterday_end = LocalDateTime.of(yesterday, LocalTime.MAX);

        return settleMapper.selectMemberCount(_start, _end, _yesterday_start, _yesterday_end;
    }

    public List<SettleDepositWithdrawVO> getDepositWithdrawList() {
        LocalDate today = LocalDate.now();
        LocalDateTime _start = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime _end = LocalDateTime.of(today, LocalTime.MAX);
        return settleMapper.selectDepositWithdrawList(_start, _end);
    }


}
