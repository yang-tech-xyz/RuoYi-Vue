package com.ruoyi.admin.service;

import com.ruoyi.admin.mapper.TopHomeMapper;
import com.ruoyi.admin.vo.HomeDepositVO;
import com.ruoyi.admin.vo.HomeMemberCountVO;
import com.ruoyi.admin.vo.HomeWithdrawVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TopHomeService {

    @Autowired
    private TopHomeMapper homeMapper;

    /**
     * 充值金额
     */
    public List<HomeDepositVO> getDepositList(String symbol, LocalDate date) {
        List<HomeDepositVO> vos = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            LocalDateTime _start = LocalDateTime.of(date.minusDays(i), LocalTime.MIN);
            LocalDateTime _end = LocalDateTime.of(date.minusDays(i), LocalTime.MAX);
            HomeDepositVO vo = new HomeDepositVO();
            vo.setDate(_start.toLocalDate());
            vo.setAmount(homeMapper.depositBySymbolAndDate(symbol, _start, _end));
            vos.add(vo);
        }
        return vos;
    }

    /**
     * 提现金额
     */
    public List<HomeWithdrawVO> getWithdrawList(String symbol, LocalDate date) {
        List<HomeWithdrawVO> vos = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            LocalDateTime _start = LocalDateTime.of(date.minusDays(i), LocalTime.MIN);
            LocalDateTime _end = LocalDateTime.of(date.minusDays(i), LocalTime.MAX);
            HomeWithdrawVO vo = new HomeWithdrawVO();
            vo.setDate(_start.toLocalDate());
            vo.setAmount(homeMapper.withdrawBySymbolAndDate(symbol, _start, _end));
            vos.add(vo);
        }
        return vos;
    }

    /**
     * 每日注册
     */
    public List<HomeMemberCountVO> getMemberCountList(LocalDate date) {
        List<HomeMemberCountVO> vos = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            LocalDateTime _start = LocalDateTime.of(date.minusDays(i), LocalTime.MIN);
            LocalDateTime _end = LocalDateTime.of(date.minusDays(i), LocalTime.MAX);
            HomeMemberCountVO vo = new HomeMemberCountVO();
            vo.setDate(_start.toLocalDate());
            vo.setCount(homeMapper.memberCountByDate(_start, _end));
            vos.add(vo);
        }
        return vos;
    }

}