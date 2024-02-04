package com.ruoyi.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountVO {

    @ApiModelProperty("会员ID")
    private Long mebId;

    @ApiModelProperty("代币")
    private String symbol;

    @ApiModelProperty("可用金额")
    private BigDecimal availableBalance;

    @ApiModelProperty("锁仓金额")
    private BigDecimal lockupBalance;

    @ApiModelProperty("冻结金额")
    private BigDecimal frozenBalance;

}
