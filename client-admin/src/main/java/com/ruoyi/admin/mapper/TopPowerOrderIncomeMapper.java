package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.dto.PowerOrderIncomePageDTO;
import com.ruoyi.admin.vo.PowerOrderIncomeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.ruoyi.admin.entity.TopPowerOrderIncome;

@Repository
public interface TopPowerOrderIncomeMapper extends BaseMapper<TopPowerOrderIncome>{

    IPage<PowerOrderIncomeVO> selectPageVO(@Param("iPage") IPage<PowerOrderIncomeVO> iPage, @Param("dto") PowerOrderIncomePageDTO dto);
}

