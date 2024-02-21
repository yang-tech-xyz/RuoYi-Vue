package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.entity.TopPowerOrderIncome;
import com.ruoyi.web.vo.OrderIncomeProcessVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopPowerOrderIncomeMapper extends BaseMapper<TopPowerOrderIncome> {

    List<OrderIncomeProcessVO> selectProcessIncomeByUserId(@Param("userId") Long userId);

    void updateProcessEnabled(@Param("userId") Long userId);
}
