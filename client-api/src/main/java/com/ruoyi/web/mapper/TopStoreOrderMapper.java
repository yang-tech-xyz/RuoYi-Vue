package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.web.entity.TopStoreOrder;
import com.ruoyi.web.vo.StoreOrderInfoVO;
import com.ruoyi.web.vo.StoreOrderVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopStoreOrderMapper extends BaseMapper<TopStoreOrder> {

    StoreOrderInfoVO selectInfoVO(@Param("wallet") String wallet);

    IPage<StoreOrderVO> selectPageVO(@Param("iPage") IPage<StoreOrderVO> iPage, @Param("wallet") String wallet);

    TopStoreOrder lockByOrderNo(@Param("orderNo") String orderNo);

}

