package com.ruoyi.web.task;

import com.ruoyi.web.service.TopStoreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StoreTask {

    @Autowired
    private TopStoreOrderService service;

    /**
     * 每日1点执行
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void redeem() {
        service.redeem();
    }
}
