package com.ruoyi.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreVO {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("产品名称")
    private String name;

    @ApiModelProperty("产品周期")
    private Integer period;

    @ApiModelProperty("存入币种")
    private String token;

    @ApiModelProperty("最小投资额")
    private BigDecimal minOrderAmount;

    @ApiModelProperty("最大投注额")
    private BigDecimal maxOrderAmount;

    @ApiModelProperty("最小收益利率")
    private BigDecimal minRate;

    @ApiModelProperty("最大收益利率")
    private BigDecimal maxRate;

}
