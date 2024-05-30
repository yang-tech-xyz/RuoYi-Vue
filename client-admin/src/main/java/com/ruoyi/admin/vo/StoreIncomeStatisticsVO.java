package com.ruoyi.admin.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreIncomeStatisticsVO {

    private String symbol;

    private BigDecimal amount = BigDecimal.ZERO;

}
