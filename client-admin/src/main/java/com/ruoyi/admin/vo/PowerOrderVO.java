package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PowerOrderVO {

    private String wallet;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "购买币种")
    private String symbol;

    @Schema(description = "购买金额")
    private BigDecimal amount;

    @Schema(description = "购买台数")
    private Integer number;

    @Schema(description = "产出币种")
    private String outputSymbol;

    @Schema(description = "产出周期,默认值360天")
    private Integer period;

    @Schema(description = "累计产出")
    private BigDecimal income;

    @Schema(description = "订单日期")
    private LocalDate orderDate;

    @Schema(description = "退出日期")
    private LocalDate endDate;

}
