package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.dto.PowerOrderIncomePageDTO;
import com.ruoyi.admin.vo.PowerOrderIncomeStatisticsVO;
import com.ruoyi.admin.vo.PowerOrderIncomeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.ruoyi.admin.entity.TopPowerOrderIncome;

import java.util.List;

@Repository
public interface TopPowerOrderIncomeMapper extends BaseMapper<TopPowerOrderIncome>{

    List<PowerOrderIncomeStatisticsVO> selectStatistics(@Param("dto") PowerOrderIncomePageDTO dto);

    IPage<PowerOrderIncomeVO> selectPageVO(@Param("iPage") IPage<PowerOrderIncomeVO> iPage, @Param("dto") PowerOrderIncomePageDTO dto);
}

