package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopMiningDailyIncome;
import com.ruoyi.web.mapper.TopMiningDailyIncomeMapper;
import com.ruoyi.web.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class TopMiningDailyIncomeService extends ServiceImpl<TopMiningDailyIncomeMapper, TopMiningDailyIncome> {

    /**
     * 用户挖矿天收益
     * 1.
     */
    @Transactional(rollbackFor = Exception.class)
    public void process(List<UserVO> userVOList, LocalDate processDate) {

    }
}
