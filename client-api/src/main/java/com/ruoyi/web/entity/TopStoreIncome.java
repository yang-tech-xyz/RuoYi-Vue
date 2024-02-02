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
@TableName(value = "top_store_income")
public class TopStoreIncome implements Serializable {
    
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户ID")
    @TableField(value = "meb_id")
    private Long mebId;
    
    @ApiModelProperty("存单号")
    @TableField(value = "store_no")
    private String storeNo;
    
    @ApiModelProperty("存币U价")
    @TableField(value = "token_price")
    private BigDecimal tokenPrice;
    
    @ApiModelProperty("收益金额")
    @TableField(value = "income")
    private BigDecimal income;
    
    @ApiModelProperty("收益比例")
    @TableField(value = "income_rate")
    private BigDecimal incomeRate;
    
    @ApiModelProperty("收益时间")
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

