package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.dto.PowerOrderSharingIncomePageDTO;
import com.ruoyi.admin.vo.PowerOrderSharingIncomeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.ruoyi.admin.entity.TopPowerSharingIncome;

@Repository
public interface TopPowerSharingIncomeMapper extends BaseMapper<TopPowerSharingIncome>{

    IPage<PowerOrderSharingIncomeVO> selectPageVO(@Param("iPage") IPage<PowerOrderSharingIncomeVO> iPage, @Param("dto") PowerOrderSharingIncomePageDTO dto);
}

