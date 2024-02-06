package com.ruoyi.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "top_account")
public class TopAccount implements Serializable {
    
    @Schema(description="主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description="会员ID")
    @TableField(value = "user_id")
    private Long userId;
    
    @Schema(description="代币")
    @TableField(value = "symbol")
    private String symbol;
    
    @Schema(description="可用金额")
    @TableField(value = "available_balance")
    private BigDecimal availableBalance;
    
    @Schema(description="锁仓金额")
    @TableField(value = "lockup_balance")
    private BigDecimal lockupBalance;
    
    @Schema(description="冻结金额")
    @TableField(value = "frozen_balance")
    private BigDecimal frozenBalance;
    
    @Schema(description="创建日期")
    @TableField(value = "created_date")
    private LocalDateTime createdDate;
    
    @Schema(description="创建人")
    @TableField(value = "created_by")
    private String createdBy;
    
    @Schema(description="更新日期")
    @TableField(value = "updated_date")
    private LocalDateTime updatedDate;
    
    @Schema(description="更新人")
    @TableField(value = "updated_by")
    private String updatedBy;
    

}

