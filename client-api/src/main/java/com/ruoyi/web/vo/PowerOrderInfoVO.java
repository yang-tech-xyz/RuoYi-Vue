package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PowerOrderInfoVO {

    @Schema(description = "已领取（BTC）")
    private BigDecimal claimIncome = BigDecimal.ZERO;

    @Schema(description = "未领取（BTC）")
    private BigDecimal unClaimIncome = BigDecimal.ZERO;

}
