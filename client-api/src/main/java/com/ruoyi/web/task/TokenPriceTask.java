package com.ruoyi.web.task;

import com.ruoyi.web.service.TopTokenPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenPriceTask {

    @Autowired
    private TopTokenPriceService service;

    /**
     * 每3秒执行一次
     */
    @Scheduled(initialDelay = 1, fixedDelay = 3000)
    public void refPrice() {
        service.refPrice();
    }
}
