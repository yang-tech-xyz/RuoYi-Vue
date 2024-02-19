package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PowerOrderVO {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "购买金额,价格乘以购买数量等于购买金额")
    private BigDecimal amount;

    @Schema(description = "购买台数")
    private Integer number;

    @Schema(description = "产出币种")
    private String outputSymbol;

    @Schema(description = "产出周期,默认值360天")
    private Integer period;

    @Schema(description = "产出率")
    private BigDecimal outputRatio;

    @Schema(description = "预估总产出")
    private BigDecimal expectedTotalOutput;

    @Schema(description = "订单日期")
    private LocalDate orderDate;

    @Schema(description = "退出日期")
    private LocalDate endDate;

    @Schema(description = "累计收益")
    private BigDecimal income;

}
