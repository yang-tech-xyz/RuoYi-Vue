package com.ruoyi.admin.entity;

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
@TableName(value = "top_power_sharing_income")
public class TopPowerSharingIncome implements Serializable {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "贡献人ID")
    @TableField(value = "provider_user_id")
    private Long providerUserId;

    @Schema(description = "贡献人订单号")
    @TableField(value = "provider_order_no")
    private String providerOrderNo;

    @Schema(description = "贡献人层级")
    @TableField(value = "provider_level")
    private Integer providerLevel;

    @Schema(description = "层级收益比例")
    @TableField(value = "rate")
    private BigDecimal rate;

    @Schema(description = "收益币种")
    @TableField(value = "symbol")
    private String symbol;

    @Schema(description = "收益金额")
    @TableField(value = "income")
    private BigDecimal income;

    @Schema(description = "收益日期")
    @TableField(value = "process_date")
    private LocalDate processDate;

    @Schema(description = "是否处理")
    @TableField(value = "process_enabled")
    private Boolean processEnabled;

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

