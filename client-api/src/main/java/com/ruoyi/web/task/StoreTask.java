package com.ruoyi.web.task;

import cn.hutool.core.util.IdUtil;
import com.ruoyi.web.enums.TopNo;
import com.ruoyi.web.service.TopStoreOrderService;
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
public class StoreTask {

    @Autowired
    private TopStoreOrderService orderService;

    @Autowired
    private TopTaskProcessService taskProcessService;


    /**
     * 理财发息
     * 1:每日凌晨1点
     */
    @Async
    @Scheduled(cron = "0 0 1 * * ?")
    public void store() {
        LocalDateTime start = LocalDateTime.now();
        log.info("【TOP - API】 -> 开始,理财发息任务执行：{}", start);
        String processNo = TopNo.PROCESS_NO._code + IdUtil.getSnowflake(TopNo.PROCESS_NO._workId).nextIdStr();
        taskProcessService.start(processNo, start.toLocalDate(), 2);
        orderService.process(start.toLocalDate());
        LocalDateTime end = LocalDateTime.now();
        taskProcessService.end(processNo);
        log.info("【TOP - API】 -> 结束,理财发息任务执行：{}，Diff seconds:{}", end, Duration.between(start, end).getSeconds());

    }

}
