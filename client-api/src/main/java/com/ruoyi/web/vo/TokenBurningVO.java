package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TokenBurningVO {

    @Schema(description = "币种")
    private String symbol;

    @Schema(description = "销毁数量")
    private BigDecimal burningAmount;

}
