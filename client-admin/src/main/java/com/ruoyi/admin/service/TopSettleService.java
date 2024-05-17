package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.admin.dto.SettleDepositWithdrawPageDTO;
import com.ruoyi.admin.dto.SettleMemberInvitePageDTO;
import com.ruoyi.admin.mapper.TopSettleMapper;
import com.ruoyi.admin.vo.*;
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

        return settleMapper.selectMemberCount(_start, _end, _yesterday_start, _yesterday_end);
    }

    public List<SettleDepositWithdrawVO> getDepositWithdrawList() {
        LocalDate today = LocalDate.now();
        LocalDateTime _start = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime _end = LocalDateTime.of(today, LocalTime.MAX);
        return settleMapper.selectDepositWithdrawList(_start, _end);
    }


    public PageVO<SettleDepositWithdrawPageVO> getDepositWithdrawPage(SettleDepositWithdrawPageDTO dto) {
        IPage<SettleDepositWithdrawPageVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = settleMapper.selectDepositWithdrawPageVO(iPage, dto);
        PageVO<SettleDepositWithdrawPageVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }

    public PageVO<SettleMemberInvitePageVO> getMemberInvitePage(SettleMemberInvitePageDTO dto) {
        IPage<SettleMemberInvitePageVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = settleMapper.selectMemberInvitePageVO(iPage, dto);
        PageVO<SettleMemberInvitePageVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }
}
