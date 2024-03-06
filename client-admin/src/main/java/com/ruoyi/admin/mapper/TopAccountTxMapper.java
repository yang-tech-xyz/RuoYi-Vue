package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.dto.AccountTxPageDTO;
import com.ruoyi.admin.dto.StoreIncomePageDTO;
import com.ruoyi.admin.entity.TopAccountTx;
import com.ruoyi.admin.vo.AccountTxVO;
import com.ruoyi.admin.vo.StoreIncomeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopAccountTxMapper extends BaseMapper<TopAccountTx> {

    IPage<AccountTxVO> selectPageVO(@Param("iPage") IPage<AccountTxVO> iPage, @Param("dto") AccountTxPageDTO dto);

    IPage<StoreIncomeVO> selectStoreIncomePageVO(@Param("iPage") IPage<StoreIncomeVO> iPage, @Param("dto") StoreIncomePageDTO dto);
}

