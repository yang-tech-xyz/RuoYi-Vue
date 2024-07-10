package com.ruoyi.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TokenBurningDTO {

    private String symbol = "BTCF";

    @Schema(description = "销毁数量")
    private BigDecimal burningAmount;

}
