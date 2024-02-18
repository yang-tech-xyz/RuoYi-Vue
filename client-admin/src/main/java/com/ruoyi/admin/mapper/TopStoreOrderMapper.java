package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.dto.StoreOrderPageDTO;
import com.ruoyi.admin.entity.TopStoreOrder;
import com.ruoyi.admin.vo.StoreOrderVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopStoreOrderMapper extends BaseMapper<TopStoreOrder> {

    IPage<StoreOrderVO> selectPageVO(@Param("iPage") IPage<StoreOrderVO> iPage, @Param("dto") StoreOrderPageDTO dto);

}

