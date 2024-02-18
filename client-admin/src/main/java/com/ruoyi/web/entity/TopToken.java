package com.ruoyi.web.entity;

import java.math.BigDecimal;
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
@TableName(value = "top_token")
public class TopToken implements Serializable {
    
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("token 名称")
    @TableField(value = "symbol")
    private String symbol;
    
    @ApiModelProperty("小数位")
    @TableField(value = "decimals")
    private Integer decimals;
    
    @ApiModelProperty("价格平台")
    @TableField(value = "plate")
    private String plate;
    
    @ApiModelProperty("是否上线: 0,上线,1下线")
    @TableField(value = "online")
    private Integer online;
    
    @ApiModelProperty("展示年利率")
    @TableField(value = "output_annual_interest_rate")
    private String outputAnnualInterestRate;
    
    @ApiModelProperty("实际年利率")
    @TableField(value = "annual_interest_rate")
    private BigDecimal annualInterestRate;
    
    @ApiModelProperty("当前最新价格")
    @TableField(value = "price")
    private BigDecimal price;
    
    @ApiModelProperty("自动刷新价格")
    @TableField(value = "auto_price_enabled")
    private Boolean autoPriceEnabled;
    
    @ApiModelProperty("是否挖矿")
    @TableField(value = "power_enabled")
    private Boolean powerEnabled;
    
    @ApiModelProperty("是否理财")
    @TableField(value = "store_enabled")
    private Boolean storeEnabled;
    
    @ApiModelProperty("${column.comment}")
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    
    @ApiModelProperty("${column.comment}")
    @TableField(value = "create_by")
    private String createBy;
    
    @ApiModelProperty("${column.comment}")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
    
    @ApiModelProperty("${column.comment}")
    @TableField(value = "update_by")
    private String updateBy;
    

}

