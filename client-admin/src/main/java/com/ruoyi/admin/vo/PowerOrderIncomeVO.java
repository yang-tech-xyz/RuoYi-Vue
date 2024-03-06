package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PowerOrderIncomeVO {

    private String wallet;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "台数")
    private Integer number;

    @Schema(description = "收益币种")
    private String symbol;

    @Schema(description = "日利率（年化利率/周期）")
    private BigDecimal rate;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "收益金额，保留10位")
    private BigDecimal income;

    @Schema(description = "是否处理")
    private Boolean processEnabled;

    @Schema(description = "处理日期")
    private LocalDate processDate;

}
