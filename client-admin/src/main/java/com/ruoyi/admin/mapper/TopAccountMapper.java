package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.dto.AccountPageDTO;
import com.ruoyi.admin.dto.AccountTxPageDTO;
import com.ruoyi.admin.entity.TopAccount;
import com.ruoyi.admin.vo.AccountTxStatisticsVO;
import com.ruoyi.admin.vo.AccountVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopAccountMapper extends BaseMapper<TopAccount> {


    TopAccount lockById(@Param("id") Long id);

    AccountTxStatisticsVO selectStatistics(@Param("dto") AccountTxPageDTO dto);

    IPage<AccountVO> selectPageVO(@Param("iPage") IPage<AccountVO> iPage, @Param("dto") AccountPageDTO dto);
}

