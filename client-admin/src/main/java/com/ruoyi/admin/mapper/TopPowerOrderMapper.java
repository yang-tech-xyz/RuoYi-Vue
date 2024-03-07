package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.dto.PowerOrderPageDTO;
import com.ruoyi.admin.entity.TopPowerOrder;
import com.ruoyi.admin.vo.PowerOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TopPowerOrderMapper extends BaseMapper<TopPowerOrder> {

    IPage<PowerOrderVO> selectPageVO(@Param("iPage") IPage<PowerOrderVO> iPage, @Param("dto") PowerOrderPageDTO dto);

}
