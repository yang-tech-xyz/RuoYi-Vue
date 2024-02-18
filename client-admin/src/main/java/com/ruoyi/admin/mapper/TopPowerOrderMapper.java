package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.admin.entity.TopPowerOrder;
import com.ruoyi.admin.vo.PowerOrderVO;
import com.ruoyi.admin.vo.TopPowerOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TopPowerOrderMapper extends BaseMapper<TopPowerOrder> {

    List<TopPowerOrder> selectOrderList(@Param("userId") Long userId, @Param("processDate") LocalDate processDate);

    IPage<PowerOrderVO> selectPageVO(@Param("iPage") IPage<PowerOrderVO> iPage, @Param("walletAddress") String walletAddress);

    Page<TopPowerOrderVO> selectPagePowerVO(@Param("page") Page page, @Param("userId") Long userId);
}
