package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountVO {

    @Schema(name="会员ID")
    private Long mebId;

    @Schema(name="代币")
    private String symbol;

    @Schema(name="可用金额")
    private BigDecimal availableBalance;

    @Schema(name="锁仓金额")
    private BigDecimal lockupBalance;

    @Schema(name="冻结金额")
    private BigDecimal frozenBalance;

}
