package com.ruoyi.admin.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SettleStatisticsVO {

    private String symbol;

    private BigDecimal deposit = BigDecimal.ZERO;

    private BigDecimal withdraw = BigDecimal.ZERO;

    private BigDecimal diff = BigDecimal.ZERO;

    public BigDecimal getDiff() {
        return deposit.subtract(withdraw);
    }
    
}