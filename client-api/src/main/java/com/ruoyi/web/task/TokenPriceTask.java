package com.ruoyi.web.task;

import com.ruoyi.web.service.TopTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenPriceTask {

    @Autowired
    private TopTokenService service;

    /**
     * 每5秒执行一次
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 5000)
    public void refPrice() {
        service.refPrice();
    }

}
