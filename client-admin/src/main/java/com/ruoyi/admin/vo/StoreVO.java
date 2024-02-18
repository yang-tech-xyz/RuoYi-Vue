package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreVO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "产品名称")
    private String name;

    @Schema(description = "产品周期（月）,每月限定30天")
    private Integer period;

    @Schema(description = "存入币种")
    private String symbol;

    @Schema(description = "收益币种")
    private String incomeSymbol;

    @Schema(description = "最低倍数投资额")
    private Integer limitOrderAmount;

    @Schema(description = "收益利率")
    private BigDecimal rate;

    private BigDecimal price = BigDecimal.ZERO;

}
