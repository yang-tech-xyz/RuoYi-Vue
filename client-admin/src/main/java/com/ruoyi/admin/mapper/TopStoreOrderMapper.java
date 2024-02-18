package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.entity.TopStoreOrder;
import com.ruoyi.admin.vo.OrderInfoVO;
import com.ruoyi.admin.vo.StoreOrderVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopStoreOrderMapper extends BaseMapper<TopStoreOrder> {

    OrderInfoVO selectInfoVO(@Param("walletAddress") String walletAddress);

    IPage<StoreOrderVO> selectPageVO(@Param("iPage") IPage<StoreOrderVO> iPage, @Param("walletAddress") String walletAddress);

    TopStoreOrder lockByOrderNo(@Param("orderNo") String orderNo);

}

