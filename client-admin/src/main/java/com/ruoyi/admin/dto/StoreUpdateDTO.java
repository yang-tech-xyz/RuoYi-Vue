package com.ruoyi.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreUpdateDTO {

    private Long id;

    @Schema(description = "产品名称")
    private String name;

    @Schema(description = "产品周期（月）,自然月")
    private Integer period;

    @Schema(description = "最低投资数量")
    private BigDecimal limitMinAmount;

    @Schema(description = "展示利率")
    private String displayRate;

    @Schema(description = "实际利率")
    private BigDecimal rate;

    @Schema(description = "状态：1=有效，2=无效")
    private Integer status;

}
