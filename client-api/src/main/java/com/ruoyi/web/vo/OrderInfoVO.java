package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderInfoVO {

    @Schema(description = "累计投资（USD）")
    private BigDecimal investAmount = BigDecimal.ZERO;

    @Schema(description = "累计利息(USD)")
    private BigDecimal interest = BigDecimal.ZERO;

}
