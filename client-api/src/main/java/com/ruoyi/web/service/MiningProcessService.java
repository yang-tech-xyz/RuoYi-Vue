package com.ruoyi.web.service;

import com.ruoyi.web.mapper.TopTokenPriceMapper;
import com.ruoyi.web.vo.TokenPriceVO;
import com.ruoyi.web.vo.UserProcessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class MiningProcessService {

    @Autowired
    private TopUserService userService;

    @Autowired
    private TopTokenPriceMapper priceMapper;

    @Autowired
    private TopPowerOrderService powerOrderService;

    @Autowired
    private TopPowerDailyIncomeService dailyIncomeService;

    @Autowired
    private TopPowerSharingIncomeService sharingIncomeService;

    @Autowired
    private TopPowerIncomeService incomeService;


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
        List<TokenPriceVO> priceVOList = priceMapper.selectListVO();
        List<UserProcessVO> userVOList = userService.getUserVOList();
        userService.process(userVOList);
        powerOrderService.process(userVOList, processDate);
        dailyIncomeService.process(userVOList, priceVOList, processDate);
        sharingIncomeService.process(userVOList, priceVOList, processDate);
        incomeService.process(userVOList, processDate);
    }
}
