package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.dto.PowerOrderSharingIncomePageDTO;
import com.ruoyi.admin.vo.PowerOrderSharingIncomeStatisticsVO;
import com.ruoyi.admin.vo.PowerOrderSharingIncomeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.ruoyi.admin.entity.TopPowerSharingIncome;

import java.util.List;

@Repository
public interface TopPowerSharingIncomeMapper extends BaseMapper<TopPowerSharingIncome>{

    List<PowerOrderSharingIncomeStatisticsVO> selectStatistics(@Param("dto") PowerOrderSharingIncomePageDTO dto);

    IPage<PowerOrderSharingIncomeVO> selectPageVO(@Param("iPage") IPage<PowerOrderSharingIncomeVO> iPage, @Param("dto") PowerOrderSharingIncomePageDTO dto);
}

