package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StoreIncomeVO {

    @Schema(description = "会员ID")
    private Long userId;

    private String wallet;

    @Schema(description = "代币")
    private String symbol;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "类型")
    private String txType;

    @Schema(description = "订单号")
    private String refNo;

    @Schema(description = "处理时间")
    private LocalDateTime createdDate;

}