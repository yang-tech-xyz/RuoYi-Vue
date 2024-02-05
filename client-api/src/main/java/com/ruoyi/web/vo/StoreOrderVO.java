package com.ruoyi.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StoreOrderVO {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("产品ID")
    private Long storeId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("存单号")
    private String orderNo;

    @ApiModelProperty("存入币种")
    private String symbol;

    @ApiModelProperty("币种价格")
    private BigDecimal price;

    @ApiModelProperty("存入金额")
    private BigDecimal amount;

    @ApiModelProperty("利率")
    private BigDecimal rate;

    @ApiModelProperty("收益币种")
    private String incomeSymbol;

    @ApiModelProperty("收益")
    private BigDecimal income;

    @ApiModelProperty("存入时间")
    private LocalDateTime storeDate;

    @ApiModelProperty("释放时间")
    private LocalDate releaseDate;

    @ApiModelProperty("领取时间")
    private LocalDateTime redeemDate;

    @ApiModelProperty("状态：1=收益中，2=已领取")
    private Integer status;


}
