package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SettleDepositWithdrawVO {

    @Schema(description = "币种")
    private String symbol;

    @Schema(description = "充值金额")
    private BigDecimal deposit = BigDecimal.ZERO;

    @Schema(description = "提现金额")
    private BigDecimal withdraw = BigDecimal.ZERO;

    @Schema(description = "差额")
    private BigDecimal diff = BigDecimal.ZERO;

    public BigDecimal getDiff() {
        return deposit.subtract(withdraw);
    }
}
