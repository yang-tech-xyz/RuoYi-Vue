package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.entity.TopPowerDailyIncome;
import com.ruoyi.web.vo.DailyIncomeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopPowerDailyIncomeMapper extends BaseMapper<TopPowerDailyIncome> {

    void updateProcessEnabled(@Param("userId") Long userId);

    List<DailyIncomeVO> selectUnclaimedList(@Param("wallet") String wallet);

    TopPowerDailyIncome lockById(@Param("id") Long id);

    List<TopPowerDailyIncome> selectUnclaimed(@Param("wallet") String wallet);
}

