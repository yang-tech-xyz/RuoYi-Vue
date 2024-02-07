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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "top_mining_sharing_income")
public class TopMiningSharingIncome implements Serializable {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "贡献人ID")
    @TableField(value = "provider_user_id")
    private Long providerUserId;

    @Schema(description = "贡献人层级")
    @TableField(value = "provider_level")
    private Integer providerLevel;

    @Schema(description = "贡献人投资金额")
    @TableField(value = "provider_amount")
    private BigDecimal providerAmount;

    @Schema(description = "贡献人收益金额")
    @TableField(value = "provider_income")
    private BigDecimal providerIncome;

    @Schema(description = "收益币种")
    @TableField(value = "income_symbol")
    private String incomeSymbol;

    @Schema(description = "收益币种价格")
    @TableField(value = "income_price")
    private BigDecimal incomePrice;

    @Schema(description = "收益比例")
    @TableField(value = "income_rate")
    private BigDecimal incomeRate;

    @Schema(description = "收益")
    @TableField(value = "income")
    private BigDecimal income;

    @Schema(description = "收益日期")
    @TableField(value = "income_date")
    private LocalDate incomeDate;

    @Schema(description = "创建日期")
    @TableField(value = "created_date")
    private LocalDateTime createdDate;

    @Schema(description = "创建人")
    @TableField(value = "created_by")
    private String createdBy;

    @Schema(description = "更新日期")
    @TableField(value = "updated_date")
    private LocalDateTime updatedDate;

    @Schema(description = "更新人")
    @TableField(value = "updated_by")
    private String updatedBy;


}

