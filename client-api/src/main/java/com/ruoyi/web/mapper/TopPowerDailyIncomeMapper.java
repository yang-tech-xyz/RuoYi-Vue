package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.entity.TopPowerDailyIncome;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopPowerDailyIncomeMapper extends BaseMapper<TopPowerDailyIncome> {

    void updateProcessEnabled(@Param("userId") Long userId);

}

