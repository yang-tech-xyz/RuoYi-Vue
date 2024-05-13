package com.ruoyi.admin.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class HomeWithdrawVO {

    private LocalDate date;

    private BigDecimal amount;

}
