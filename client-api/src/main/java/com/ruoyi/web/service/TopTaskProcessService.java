package com.ruoyi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopTaskProcess;
import com.ruoyi.web.enums.Status;
import com.ruoyi.web.mapper.TopTaskProcessMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
public class TopTaskProcessService extends ServiceImpl<TopTaskProcessMapper, TopTaskProcess> {

    public void start(String processNo, LocalDate processDate) {
        TopTaskProcess taskProcess = new TopTaskProcess();
        taskProcess.setProcessNo(processNo);
        taskProcess.setTaskStartDate(LocalDateTime.now());
        taskProcess.setProcessDate(processDate);
        taskProcess.setStatus(Status._1._value);
        taskProcess.setCreatedBy("SYSTEM");
        taskProcess.setCreatedDate(LocalDateTime.now());
        taskProcess.setUpdatedBy("SYSTEM");
        taskProcess.setUpdatedDate(LocalDateTime.now());
        baseMapper.insert(taskProcess);
    }

    public void end(String processNo) {
        TopTaskProcess taskProcess = baseMapper.selectOne(new LambdaQueryWrapper<TopTaskProcess>()
                .eq(TopTaskProcess::getProcessNo, processNo));
        taskProcess.setTaskEndDate(LocalDateTime.now());
        taskProcess.setStatus(Status._2._value);
        taskProcess.setUpdatedBy("SYSTEM");
        taskProcess.setUpdatedDate(LocalDateTime.now());
        baseMapper.updateById(taskProcess);
    }
}
