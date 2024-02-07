package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StoreOrderVO {

    @Schema(description="主键")
    private Long id;

    @Schema(description="产品ID")
    private Long storeId;

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="存单号")
    private String orderNo;

    @Schema(description="存入币种")
    private String symbol;

    @Schema(description="币种价格")
    private BigDecimal price;

    @Schema(description="存入金额")
    private BigDecimal amount;

    @Schema(description="利率")
    private BigDecimal rate;

    @Schema(description="收益币种")
    private String incomeSymbol;

    @Schema(description="收益")
    private BigDecimal income;

    @Schema(description="存入时间")
    private LocalDateTime storeDate;

    @Schema(description="释放时间")
    private LocalDate releaseDate;

    @Schema(description="领取时间")
    private LocalDateTime redeemDate;

    @Schema(description="状态：1=收益中，2=已领取")
    private Integer status;


}
