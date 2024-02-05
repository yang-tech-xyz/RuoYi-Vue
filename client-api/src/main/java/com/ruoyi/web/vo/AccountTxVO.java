package com.ruoyi.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountTxVO {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("会员ID")
    private Long userId;

    @ApiModelProperty("资产ID")
    private Long accountId;

    @ApiModelProperty("代币")
    private String symbol;

    @ApiModelProperty("交易金额")
    private BigDecimal amount;

    @ApiModelProperty("手续费")
    private BigDecimal fee;

    @ApiModelProperty("变动之前")
    private BigDecimal balanceBefore;

    @ApiModelProperty("变动之后")
    private BigDecimal balanceAfter;

    @ApiModelProperty("流水号")
    private String transactionNo;

    @ApiModelProperty("资产类型(available = 可用, frozen = 冻结,lockup = 限制)")
    private String balanceType;

    @ApiModelProperty("流水类型")
    private String txType;

    @ApiModelProperty("第三方号")
    private String refNo;

    @ApiModelProperty("备注")
    private String remark;

}
