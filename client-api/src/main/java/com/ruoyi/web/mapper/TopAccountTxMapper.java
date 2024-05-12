package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.web.dto.AccountTxPageDTO;
import com.ruoyi.web.entity.TopAccountTx;
import com.ruoyi.web.vo.AccountTxVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface TopAccountTxMapper extends BaseMapper<TopAccountTx> {

    IPage<AccountTxVO> selectPageVO(@Param("iPage") IPage<AccountTxVO> iPage, @Param("walletAddress") String walletAddress, @Param("dto") AccountTxPageDTO dto);

    @Select("SELECT IFNULL(SUM(amount),0) from top_account_tx tct where tct.symbol = #{exchangeSymbol} and tct.tx_type = 'EXCHANGE' AND AMOUNT>0 AND created_date  > #{startDate} AND created_date  < #{endDate}")
    BigDecimal sumExchangeAmount(String exchangeSymbol, LocalDate startDate, LocalDate endDate);
}

