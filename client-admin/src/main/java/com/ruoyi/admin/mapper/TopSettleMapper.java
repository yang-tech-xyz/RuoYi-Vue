package com.ruoyi.admin.mapper;

import com.ruoyi.admin.vo.SettleStatisticsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TopSettleMapper {
    List<SettleStatisticsVO> selectStatistics(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
