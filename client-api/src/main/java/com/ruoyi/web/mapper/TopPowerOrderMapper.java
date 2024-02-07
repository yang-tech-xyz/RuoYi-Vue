package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.entity.TopPowerOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TopPowerOrderMapper extends BaseMapper<TopPowerOrder> {

    List<TopPowerOrder> selectOrderList(@Param("userId") Long userId, @Param("processDate") LocalDate processDate);

}
