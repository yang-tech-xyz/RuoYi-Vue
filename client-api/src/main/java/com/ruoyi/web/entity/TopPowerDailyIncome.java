package com.ruoyi.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "top_power_daily_income")
public class TopPowerDailyIncome implements Serializable {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @ApiModelProperty("订单号")
    @TableField(value = "order_no")
    private String orderNo;

    @ApiModelProperty("存入金额")
    @TableField(value = "amount")
    private BigDecimal amount;

    @ApiModelProperty("收益币种")
    @TableField(value = "income_symbol")
    private String incomeSymbol;

    @ApiModelProperty("币种价格")
    @TableField(value = "income_price")
    private BigDecimal incomePrice;

    @ApiModelProperty("比列")
    @TableField(value = "income_rate")
    private BigDecimal incomeRate;

    @ApiModelProperty("收益金额USD")
    @TableField(value = "income_usd")
    private BigDecimal incomeUsd;

    @ApiModelProperty("币种收益金额")
    @TableField(value = "income")
    private BigDecimal income;

    @ApiModelProperty("收益日期")
    @TableField(value = "income_date")
    private LocalDate incomeDate;

    @Schema(description = "是否处理")
    @TableField(value = "process_enabled")
    private Boolean processEnabled;


    @ApiModelProperty("创建日期")
    @TableField(value = "created_date")
    private LocalDateTime createdDate;

    @ApiModelProperty("创建人")
    @TableField(value = "created_by")
    private String createdBy;

    @ApiModelProperty("更新日期")
    @TableField(value = "updated_date")
    private LocalDateTime updatedDate;

    @ApiModelProperty("更新人")
    @TableField(value = "updated_by")
    private String updatedBy;


}

