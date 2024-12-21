package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 出入金统计使用的视图类
 * 注意：出入金都是用的同一个VO
 */
@Data
public class StatisticTransactionVO {

    /**
     * 提现数量
     */
    @Schema(description = "提现数量")
    private BigDecimal withdrawAmount;


    @Schema(description = "充值数量")
    private BigDecimal rechargeAmount;

    @Schema(description = "充值币种")
    private String symbol;

    @Schema(description = "操作日期")
    private String operateDate;

}
