package com.ruoyi.web.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MiningTask {


    /**
     * 矿池挖矿
     * 1.产生个人收益
     * 2.产生邀请收益（烧伤机制：每人台数限定为层数）
     * 3.发放对应的BTC换算USDT
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 3000)
    public void mining() {
    }

}
