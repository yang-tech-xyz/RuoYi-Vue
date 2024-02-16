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
@TableName(value = "top_account_tx")
public class TopAccountTx implements Serializable {
    
    @Schema(description="主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description="会员ID")
    @TableField(value = "user_id")
    private Long userId;
    
    @Schema(description="资产ID")
    @TableField(value = "account_id")
    private Long accountId;
    
    @Schema(description="代币")
    @TableField(value = "symbol")
    private String symbol;
    
    @Schema(description="交易金额")
    @TableField(value = "amount")
    private BigDecimal amount;
    
    @Schema(description="手续费")
    @TableField(value = "fee")
    private BigDecimal fee;
    
    @Schema(description="变动之前")
    @TableField(value = "balance_before")
    private BigDecimal balanceBefore;
    
    @Schema(description="变动之后")
    @TableField(value = "balance_after")
    private BigDecimal balanceAfter;
    
    @Schema(description="流水号")
    @TableField(value = "transaction_no")
    private String transactionNo;
    
    @Schema(description="资产类型(available = 可用, frozen = 冻结,lockup = 限制)")
    @TableField(value = "balance_type")
    private String balanceType;
    
    @Schema(description="流水类型")
    @TableField(value = "tx_type")
    private String txType;
    
    @Schema(description="第三方号")
    @TableField(value = "ref_no")
    private String refNo;
    
    @Schema(description="唯一号，用于幂等")
    @TableField(value = "unique_id")
    private String uniqueId;
    
    @Schema(description="备注")
    @TableField(value = "remark")
    private String remark;
    
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

