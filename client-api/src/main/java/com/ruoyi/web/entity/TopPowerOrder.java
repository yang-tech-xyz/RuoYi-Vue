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
@TableName(value = "top_power_order")
public class TopPowerOrder implements Serializable {
    
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户ID")
    @TableField(value = "user_id")
    private Long userId;
    
    @ApiModelProperty("订单编号")
    @TableField(value = "order_no")
    private String orderNo;
    
    @ApiModelProperty("购买金额,价格乘以购买数量等于购买金额")
    @TableField(value = "amount")
    private BigDecimal amount;
    
    @ApiModelProperty("购买台数")
    @TableField(value = "number")
    private Integer number;
    
    @ApiModelProperty("产出币种")
    @TableField(value = "output_symbol")
    private String outputSymbol;
    
    @ApiModelProperty("产出周期,默认值360天")
    @TableField(value = "period")
    private Integer period;
    
    @ApiModelProperty("产出率")
    @TableField(value = "output_ratio")
    private BigDecimal outputRatio;
    
    @ApiModelProperty("预估总产出")
    @TableField(value = "expected_total_output")
    private BigDecimal expectedTotalOutput;
    
    @ApiModelProperty("订单日期")
    @TableField(value = "order_date")
    private LocalDate orderDate;
    
    @ApiModelProperty("退出日期")
    @TableField(value = "end_date")
    private LocalDate endDate;
    
    @ApiModelProperty("创建时间")
    @TableField(value = "create_by")
    private String createBy;
    
    @ApiModelProperty("创建人")
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    
    @ApiModelProperty("${column.comment}")
    @TableField(value = "update_by")
    private String updateBy;
    
    @ApiModelProperty("${column.comment}")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
    

}

