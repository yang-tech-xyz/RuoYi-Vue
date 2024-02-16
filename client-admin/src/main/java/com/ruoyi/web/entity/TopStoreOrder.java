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
    
    @ApiModelProperty("存入数量")
    @TableField(value = "amount")
    private BigDecimal amount;
    
    @ApiModelProperty("投资金额USD：币种数量*币种价格")
    @TableField(value = "invest_amount")
    private BigDecimal investAmount;
    
    @ApiModelProperty("订单时间")
    @TableField(value = "order_date")
    private LocalDateTime orderDate;
    
    @ApiModelProperty("到期释放时间：订单时间+周期")
    @TableField(value = "release_date")
    private LocalDate releaseDate;
    
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

