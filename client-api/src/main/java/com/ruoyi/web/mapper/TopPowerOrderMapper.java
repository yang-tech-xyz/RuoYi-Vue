package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.web.entity.TopPowerOrder;
import com.ruoyi.web.vo.PowerOrderInfoVO;
import com.ruoyi.web.vo.PowerOrderVO;
import com.ruoyi.web.vo.TopPowerOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.ArrayList;

@Mapper
public interface TopPowerOrderMapper extends BaseMapper<TopPowerOrder> {

    ArrayList<TopPowerOrder> selectOrderList(@Param("userId") Long userId, @Param("processDate") LocalDate processDate);

    IPage<PowerOrderVO> selectPageVO(@Param("iPage") IPage<PowerOrderVO> iPage, @Param("walletAddress") String walletAddress);

    Page<TopPowerOrderVO> selectPagePowerVO(@Param("page") Page page, @Param("userId") Long userId);

    Integer sumPowerNumberByUserId(@Param("userId") Long userId);

    PowerOrderInfoVO selectInfo(@Param("wallet") String wallet);
}
