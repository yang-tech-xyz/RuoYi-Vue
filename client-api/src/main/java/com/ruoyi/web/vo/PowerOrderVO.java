package com.ruoyi.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PowerOrderVO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("购买金额,价格乘以购买数量等于购买金额")
    private BigDecimal amount;

    @ApiModelProperty("购买台数")
    private Integer number;

    @ApiModelProperty("产出币种")
    private String outputSymbol;

    @ApiModelProperty("产出周期,默认值360天")
    private Integer period;

    @ApiModelProperty("产出率")
    private BigDecimal outputRatio;

    @ApiModelProperty("预估总产出")
    private BigDecimal expectedTotalOutput;

    @ApiModelProperty("订单日期")
    private LocalDate orderDate;

    @ApiModelProperty("退出日期")
    private LocalDate endDate;

}
