package com.ruoyi.web.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "top_account")
public class TopAccount implements Serializable {
    
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("会员ID")
    @TableField(value = "meb_id")
    private Long mebId;
    
    @ApiModelProperty("代币")
    @TableField(value = "token")
    private String token;
    
    @ApiModelProperty("可用金额")
    @TableField(value = "available_balance")
    private BigDecimal availableBalance;
    
    @ApiModelProperty("锁仓金额")
    @TableField(value = "lockup_balance")
    private BigDecimal lockupBalance;
    
    @ApiModelProperty("冻结金额")
    @TableField(value = "frozen_balance")
    private BigDecimal frozenBalance;
    
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

