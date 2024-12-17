package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StatisticTransactionVO {

    /**
     * 提现数量
     */
    @Schema(description = "提现数量")
    private BigDecimal withdrawAmount;

    @Schema(description = "提现日期")
    private String withdrawCreatedDate;

    @Schema(description = "提现币种")
    private String withdrawSymbol;

    @Schema(description = "提现操作")
    private String withdrawOperation;


    @Schema(description = "充值数量")
    private BigDecimal rechargeAmount;

    @Schema(description = "充值日期")
    private String rechargeCreatedDate;

    @Schema(description = "充值币种")
    private String rechargeSymbol;

    @Schema(description = "充值操作")
    private String rechargeOperation;

}
