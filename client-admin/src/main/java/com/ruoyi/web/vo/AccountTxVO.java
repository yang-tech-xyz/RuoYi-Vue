package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountTxVO {

    @Schema(description="主键")
    private Long id;

    @Schema(description="会员ID")
    private Long userId;

    @Schema(description="资产ID")
    private Long accountId;

    @Schema(description="代币")
    private String symbol;

    @Schema(description="交易金额")
    private BigDecimal amount;

    @Schema(description="手续费")
    private BigDecimal fee;

    @Schema(description="变动之前")
    private BigDecimal balanceBefore;

    @Schema(description="变动之后")
    private BigDecimal balanceAfter;

    @Schema(description="流水号")
    private String transactionNo;

    @Schema(description="资产类型(available = 可用, frozen = 冻结,lockup = 限制)")
    private String balanceType;

    @Schema(description="流水类型")
    private String txType;

    @Schema(description="第三方号")
    private String refNo;

    @Schema(description="备注")
    private String remark;

}
