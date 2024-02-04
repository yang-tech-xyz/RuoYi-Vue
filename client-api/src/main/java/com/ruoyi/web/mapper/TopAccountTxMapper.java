package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.web.dto.AccountTxPageDTO;
import com.ruoyi.web.vo.AccountTxVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.ruoyi.web.entity.TopAccountTx;

@Repository
public interface TopAccountTxMapper extends BaseMapper<TopAccountTx>{

    IPage<AccountTxVO> selectPageVO(@Param("iPage") IPage<AccountTxVO> iPage, @Param("mebId") Long mebId, @Param("dto") AccountTxPageDTO dto);
}

