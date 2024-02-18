package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountTxVO {

    private String wallet;

    @Schema(description = "会员ID")
    private Long userId;

    @Schema(description = "资产ID")
    private Long accountId;

    @Schema(description = "代币")
    private String symbol;

    @Schema(description = "交易金额")
    private BigDecimal amount;

    @Schema(description = "手续费")
    private BigDecimal fee;

    @Schema(description = "变动之前")
    private BigDecimal balanceBefore;

    @Schema(description = "变动之后")
    private BigDecimal balanceAfter;

    @Schema(description = "流水号")
    private String transactionNo;

    @Schema(description = "资产类型(available = 可用, frozen = 冻结,lockup = 限制)")
    private String balanceType;

    @Schema(description = "流水类型")
    private String txType;

    @Schema(description = "第三方号")
    private String refNo;

    @Schema(description = "唯一号，用于幂等")
    private String uniqueId;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建日期")
    private LocalDateTime createdDate;

    @Schema(description = "更新日期")
    private LocalDateTime updatedDate;

}
