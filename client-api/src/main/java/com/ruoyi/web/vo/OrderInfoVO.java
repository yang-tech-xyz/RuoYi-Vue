package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderInfoVO {

    @Schema(name="累计投资金额")
    private BigDecimal amount = BigDecimal.ZERO;

    @Schema(name="待领取收益")
    private BigDecimal redeem = BigDecimal.ZERO;

    @Schema(name="已领取收益")
    private BigDecimal income = BigDecimal.ZERO;
    
}
