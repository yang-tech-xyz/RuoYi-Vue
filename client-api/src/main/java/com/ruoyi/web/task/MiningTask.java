package com.ruoyi.web.task;

import com.ruoyi.web.service.TopTokenPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MiningTask {

    @Autowired
    private TopTokenPriceService service;

    /**
     * 矿池挖矿
     * 1.产生个人收益
     * 2.产生邀请收益（烧伤机制：每人台数限定为层数）
     * 3.发放对应的BTC换算USDT
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 3000)
    public void refPrice() {
        service.refPrice();
    }

}
