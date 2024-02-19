package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TokenPriceVO {

    @Schema(description = "币种")
    private String symbol;

    @Schema(description = "价格")
    private BigDecimal price;

}
