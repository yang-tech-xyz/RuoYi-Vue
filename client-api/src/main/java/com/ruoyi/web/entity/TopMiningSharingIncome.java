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
@TableName(value = "top_mining_sharing_income")
public class TopMiningSharingIncome implements Serializable {
    
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户ID")
    @TableField(value = "user_id")
    private Long userId;
    
    @ApiModelProperty("贡献人ID")
    @TableField(value = "provider_user_id")
    private Long providerUserId;
    
    @ApiModelProperty("贡献人层级")
    @TableField(value = "provider_level")
    private Integer providerLevel;
    
    @ApiModelProperty("贡献人投资金额")
    @TableField(value = "provider_amount")
    private BigDecimal providerAmount;
    
    @ApiModelProperty("贡献人收益金额")
    @TableField(value = "provider_income")
    private BigDecimal providerIncome;
    
    @ApiModelProperty("收益币种")
    @TableField(value = "income_symbol")
    private String incomeSymbol;
    
    @ApiModelProperty("收益币种价格")
    @TableField(value = "income_price")
    private BigDecimal incomePrice;
    
    @ApiModelProperty("收益比例")
    @TableField(value = "income_rate")
    private BigDecimal incomeRate;
    
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

