package com.ruoyi.admin.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PowerOrderSharingIncomeStatisticsVO {

    private String symbol;

    private BigDecimal income = BigDecimal.ZERO;

}
