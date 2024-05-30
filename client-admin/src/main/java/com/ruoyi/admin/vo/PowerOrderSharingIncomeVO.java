package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PowerOrderSharingIncomeVO {

    private Long userId;

    @Schema(description = "地址")
    private String wallet;

    @Schema(description = "贡献人地址")
    private String providerWallet;

    @Schema(description = "贡献人订单号")
    private String providerOrderNo;

    @Schema(description = "贡献人层级")
    private Integer providerLevel;

    @Schema(description = "层级收益比例")
    private BigDecimal rate;

    @Schema(description = "收益币种")
    private String symbol;

    @Schema(description = "收益金额")
    private BigDecimal income;

    @Schema(description = "收益日期")
    private LocalDate processDate;

    @Schema(description = "是否处理")
    private Boolean processEnabled;

}