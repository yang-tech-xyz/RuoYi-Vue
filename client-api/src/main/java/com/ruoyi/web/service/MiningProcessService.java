package com.ruoyi.web.service;

import com.ruoyi.web.entity.TopToken;
import com.ruoyi.web.vo.UserProcessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MiningProcessService {

    @Autowired
    private TopUserService userService;

    @Autowired
    private TopTokenService tokenService;

    @Autowired
    private TopPowerOrderService powerOrderService;

    @Autowired
    private TopPowerOrderIncomeService orderIncomeService;

    @Autowired
    private TopPowerSharingIncomeService sharingIncomeService;

    @Autowired
    private TopPowerDailyIncomeService dailyIncomeService;


    /**
     * 执行总线任务
     * 1.查询所有用户
     * 2.拉取所有用户订单
     * 3.计算挖矿收益
     * 4.计算层级收益
     * 5.入账资产
     */
    public void process(LocalDate processDate) {
        Map<String, TopToken> tokens = tokenService.getTopToken();
        List<UserProcessVO> userVOList = userService.getUserVOList();
        userService.process(userVOList);
        powerOrderService.process(userVOList, processDate);
        orderIncomeService.process(userVOList, tokens, processDate);
        sharingIncomeService.process(userVOList, tokens, processDate);
        dailyIncomeService.process(userVOList, processDate);
    }
}
