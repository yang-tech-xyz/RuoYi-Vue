package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.web.vo.StoreOrderVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.ruoyi.web.entity.TopStoreOrder;

@Repository
public interface TopStoreOrderMapper extends BaseMapper<TopStoreOrder>{

    IPage<StoreOrderVO> selectPageVO(@Param("iPage") IPage<StoreOrderVO> iPage, @Param("mebId") Long mebId);

    TopStoreOrder lockByOrderNo(@Param("orderNo") String orderNo);
}

