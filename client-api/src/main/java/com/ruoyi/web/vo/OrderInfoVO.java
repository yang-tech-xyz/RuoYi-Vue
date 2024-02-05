package com.ruoyi.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderInfoVO {

    @ApiModelProperty("累计投资金额")
    private BigDecimal amount = BigDecimal.ZERO;

    @ApiModelProperty("待领取收益")
    private BigDecimal redeem = BigDecimal.ZERO;

    @ApiModelProperty("已领取收益")
    private BigDecimal income = BigDecimal.ZERO;
    
}
