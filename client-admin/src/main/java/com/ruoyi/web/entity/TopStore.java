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
@TableName(value = "top_store")
public class TopStore implements Serializable {
    
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("产品名称")
    @TableField(value = "name")
    private String name;
    
    @ApiModelProperty("产品周期（月）,自然月")
    @TableField(value = "period")
    private Integer period;
    
    @ApiModelProperty("最低投资数量")
    @TableField(value = "limit_min_amount")
    private BigDecimal limitMinAmount;
    
    @ApiModelProperty("展示利率")
    @TableField(value = "display_rate")
    private String displayRate;
    
    @ApiModelProperty("实际利率")
    @TableField(value = "rate")
    private BigDecimal rate;
    
    @ApiModelProperty("状态：1=有效，2=无效")
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

