package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.vo.DailyIncomeProcessVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.ruoyi.admin.entity.TopPowerDailyIncome;

import java.util.List;

@Repository
public interface TopPowerDailyIncomeMapper extends BaseMapper<TopPowerDailyIncome>{

    List<DailyIncomeProcessVO> selectProcessIncomeByUserId(@Param("userId") Long userId);

    void updateProcessEnabled(@Param("userId") Long userId);
}

