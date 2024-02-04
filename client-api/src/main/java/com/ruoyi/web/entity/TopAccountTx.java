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
@TableName(value = "top_account_tx")
public class TopAccountTx implements Serializable {
    
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("会员ID")
    @TableField(value = "meb_id")
    private Long mebId;
    
    @ApiModelProperty("资产ID")
    @TableField(value = "account_id")
    private Long accountId;
    
    @ApiModelProperty("代币")
    @TableField(value = "token")
    private String token;
    
    @ApiModelProperty("交易金额")
    @TableField(value = "amount")
    private BigDecimal amount;
    
    @ApiModelProperty("手续费")
    @TableField(value = "fee")
    private BigDecimal fee;
    
    @ApiModelProperty("变动之前")
    @TableField(value = "balance_before")
    private BigDecimal balanceBefore;
    
    @ApiModelProperty("变动之后")
    @TableField(value = "balance_after")
    private BigDecimal balanceAfter;
    
    @ApiModelProperty("流水号")
    @TableField(value = "transaction_no")
    private String transactionNo;
    
    @ApiModelProperty("资产类型(available = 可用, frozen = 冻结,lockup = 限制)")
    @TableField(value = "balance_type")
    private String balanceType;
    
    @ApiModelProperty("流水类型")
    @TableField(value = "tx_type")
    private String txType;
    
    @ApiModelProperty("第三方号")
    @TableField(value = "ref_no")
    private String refNo;
    
    @ApiModelProperty("唯一号，用于幂等")
    @TableField(value = "unique_id")
    private String uniqueId;
    
    @ApiModelProperty("备注")
    @TableField(value = "remark")
    private String remark;
    
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

