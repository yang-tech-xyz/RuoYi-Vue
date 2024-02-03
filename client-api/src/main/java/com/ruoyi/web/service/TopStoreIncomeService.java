package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopStoreIncome;
import com.ruoyi.web.mapper.TopStoreIncomeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TopStoreIncomeService extends ServiceImpl<TopStoreIncomeMapper, TopStoreIncome> {

    /**
     * 收益计算
     * 1.获取最新币种价格
     * 2.计算每个订单的收益
     * 3.到期订单本金+利息一起给付
     */
    @Transactional(rollbackFor = Exception.class)
    public void compute() {

    }

}
