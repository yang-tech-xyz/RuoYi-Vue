package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopMiningSharingIncome;
import com.ruoyi.web.mapper.TopMiningSharingIncomeMapper;
import com.ruoyi.web.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class TopMiningSharingIncomeService extends ServiceImpl<TopMiningSharingIncomeMapper, TopMiningSharingIncome> {

    /**
     * 层级收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void process(List<UserVO> userVOList, LocalDate processDate) {

    }
}
