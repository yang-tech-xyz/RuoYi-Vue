package com.ruoyi.admin.service;

import com.ruoyi.admin.mapper.TopSettleMapper;
import com.ruoyi.admin.vo.SettleStatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TopSettleService {

    @Autowired
    private TopSettleMapper settleMapper;

    /**
     * 结算统计
     */
    public List<SettleStatisticsVO> getStatistics(LocalDateTime start, LocalDateTime end) {
        return settleMapper.selectStatistics(start, end);
    }
}
