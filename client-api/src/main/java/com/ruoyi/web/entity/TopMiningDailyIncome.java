package com.ruoyi.web.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.*;
import java.math.BigDecimal;
import java.io.Serializable;
import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "top_mining_daily_income")
public class TopMiningDailyIncome implements Serializable {
    
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
    
    @ApiModelProperty("BTC价格")
    @TableField(value = "price")
    private BigDecimal price;
    
    @ApiModelProperty("比列")
    @TableField(value = "rate")
    private BigDecimal rate;
    
    @ApiModelProperty("收益")
    @TableField(value = "income")
    private BigDecimal income;
    
    @ApiModelProperty("收益日期")
    @TableField(value = "income_date")
    private LocalDate incomeDate;
    
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

