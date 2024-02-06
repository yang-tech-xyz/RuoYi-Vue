package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountTxVO {

    @Schema(name="主键")
    private Long id;

    @Schema(name="会员ID")
    private Long userId;

    @Schema(name="资产ID")
    private Long accountId;

    @Schema(name="代币")
    private String symbol;

    @Schema(name="交易金额")
    private BigDecimal amount;

    @Schema(name="手续费")
    private BigDecimal fee;

    @Schema(name="变动之前")
    private BigDecimal balanceBefore;

    @Schema(name="变动之后")
    private BigDecimal balanceAfter;

    @Schema(name="流水号")
    private String transactionNo;

    @Schema(name="资产类型(available = 可用, frozen = 冻结,lockup = 限制)")
    private String balanceType;

    @Schema(name="流水类型")
    private String txType;

    @Schema(name="第三方号")
    private String refNo;

    @Schema(name="备注")
    private String remark;

}
