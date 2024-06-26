package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountVO {

    @Schema(description="代币")
    private String symbol;

    @Schema(description="可用金额")
    private BigDecimal availableBalance;

    @Schema(description="锁仓金额")
    private BigDecimal lockupBalance;

    @Schema(description="冻结金额")
    private BigDecimal frozenBalance;

}
