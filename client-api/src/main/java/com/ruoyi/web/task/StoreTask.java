package com.ruoyi.web.task;

import com.ruoyi.web.service.TopStoreIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StoreTask {

    @Autowired
    private TopStoreIncomeService incomeService;

    /**
     * 每天0点计算收益
     * 1.到期自动赎回：本金+利息
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void compute() {
        incomeService.compute();
    }
}
