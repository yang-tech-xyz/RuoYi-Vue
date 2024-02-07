package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.entity.TopPowerSharingIncome;
import com.ruoyi.web.vo.SharingIncomeProcessVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopPowerSharingIncomeMapper extends BaseMapper<TopPowerSharingIncome> {

    List<SharingIncomeProcessVO> selectProcessIncomeByUserId(@Param("userId") Long userId);

    void updateProcessEnabled(@Param("userId") Long userId);
}

