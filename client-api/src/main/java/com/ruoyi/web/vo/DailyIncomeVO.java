package com.ruoyi.web.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DailyIncomeVO {

    private Long id;

    private String symbol;

    private BigDecimal income;

    private LocalDate processDate;

}