package com.ruoyi.web.task;

import cn.hutool.core.util.IdUtil;
import com.ruoyi.web.enums.TopNo;
import com.ruoyi.web.service.MiningProcessService;
import com.ruoyi.web.service.TopTaskProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Component
public class MiningTask {

    @Autowired
    private MiningProcessService processService;

    @Autowired
    private TopTaskProcessService taskProcessService;


    /**
     * 矿池挖矿
     * 1.产生个人收益
     * 2.产生邀请收益（烧伤机制：每人台数限定为层数）
     * 3.发放对应的BTC换算USDT
     */
    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void mining() {
        LocalDateTime start = LocalDateTime.now();
        log.info("【TOP - API】 -> 开始,收益总线任务执行：{}", start);
        String processNo = TopNo.PROCESS_NO._code + IdUtil.getSnowflake(TopNo.PROCESS_NO._workId).nextIdStr();
        taskProcessService.start(processNo, start.toLocalDate());
        processService.process(start.toLocalDate());
        LocalDateTime end = LocalDateTime.now();
        taskProcessService.end(processNo);
        log.info("【TOP - API】 -> 结束,收益总线任务执行：{}，Diff seconds:{}", end, Duration.between(start, end).getSeconds());

    }

}
