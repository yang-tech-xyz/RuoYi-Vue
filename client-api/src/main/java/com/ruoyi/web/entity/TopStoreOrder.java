package com.ruoyi.web.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "top_store_order")
public class TopStoreOrder implements Serializable {
    
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("产品ID")
    @TableField(value = "store_id")
    private Long storeId;
    
    @ApiModelProperty("用户ID")
    @TableField(value = "user_id")
    private Long userId;
    
    @ApiModelProperty("存单号")
    @TableField(value = "order_no")
    private String orderNo;
    
    @ApiModelProperty("存入币种")
    @TableField(value = "symbol")
    private String symbol;
    
    @ApiModelProperty("币种价格")
    @TableField(value = "price")
    private BigDecimal price;
    
    @ApiModelProperty("存入金额")
    @TableField(value = "amount")
    private BigDecimal amount;
    
    @ApiModelProperty("利率")
    @TableField(value = "rate")
    private BigDecimal rate;
    
    @ApiModelProperty("收益币种")
    @TableField(value = "income_symbol")
    private String incomeSymbol;
    
    @ApiModelProperty("收益")
    @TableField(value = "income")
    private BigDecimal income;
    
    @ApiModelProperty("存入时间")
    @TableField(value = "store_date")
    private LocalDateTime storeDate;
    
    @ApiModelProperty("释放时间")
    @TableField(value = "release_date")
    private LocalDate releaseDate;
    
    @ApiModelProperty("领取时间")
    @TableField(value = "redeem_date")
    private LocalDateTime redeemDate;
    
    @ApiModelProperty("状态：1=收益中，2=已领取")
    @TableField(value = "status")
    private Integer status;
    
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

