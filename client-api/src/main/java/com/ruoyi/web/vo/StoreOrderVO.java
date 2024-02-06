package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StoreOrderVO {

    @Schema(name="主键")
    private Long id;

    @Schema(name="产品ID")
    private Long storeId;

    @Schema(name="用户ID")
    private Long userId;

    @Schema(name="存单号")
    private String orderNo;

    @Schema(name="存入币种")
    private String symbol;

    @Schema(name="币种价格")
    private BigDecimal price;

    @Schema(name="存入金额")
    private BigDecimal amount;

    @Schema(name="利率")
    private BigDecimal rate;

    @Schema(name="收益币种")
    private String incomeSymbol;

    @Schema(name="收益")
    private BigDecimal income;

    @Schema(name="存入时间")
    private LocalDateTime storeDate;

    @Schema(name="释放时间")
    private LocalDate releaseDate;

    @Schema(name="领取时间")
    private LocalDateTime redeemDate;

    @Schema(name="状态：1=收益中，2=已领取")
    private Integer status;


}
