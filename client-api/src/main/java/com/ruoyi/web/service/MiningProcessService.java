package com.ruoyi.web.service;

import com.ruoyi.web.mapper.TopUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
public class MiningProcessService {

    @Autowired
    private TopUserMapper userMapper;

    @Autowired
    private TopMiningDailyIncomeService dailyIncomeService;

    @Autowired
    private TopMiningSharingIncomeService sharingIncomeService;


    /**
     * 执行总线任务
     * 1.查询所有用户
     * 2.拉取所有用户订单
     * 3.计算挖矿收益
     * 4.计算层级收益
     * 5.入账资产
     */
    @Transactional(rollbackFor = Exception.class)
    public void process(LocalDate processDate) {
        LocalDateTime processDateTime = LocalDateTime.of(processDate, LocalTime.MIN);


    }
}
