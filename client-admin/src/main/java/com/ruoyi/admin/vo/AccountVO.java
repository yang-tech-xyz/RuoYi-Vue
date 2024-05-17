package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountVO {

    @Schema(description = "会员ID")
    private Long userId;

    private String wallet;

    @Schema(description = "代币")
    private String symbol;

    @Schema(description = "可用金额")
    private BigDecimal availableBalance;

    @Schema(description = "锁仓金额")
    private BigDecimal lockupBalance;

    @Schema(description = "冻结金额")
    private BigDecimal frozenBalance;

    @Schema(description = "创建日期")
    private LocalDateTime createdDate;

    @Schema(description = "更新日期")
    private LocalDateTime updatedDate;

}
