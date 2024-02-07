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
@TableName(value = "top_power_order")
public class TopPowerOrder implements Serializable {

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "订单编号")
    @TableField(value = "order_no")
    private String orderNo;

    @Schema(description = "购买金额,价格乘以购买数量等于购买金额")
    @TableField(value = "amount")
    private BigDecimal amount;

    @Schema(description = "购买台数")
    @TableField(value = "number")
    private Integer number;

    @Schema(description = "产出币种")
    @TableField(value = "output_symbol")
    private String outputSymbol;

    @Schema(description = "产出周期,默认值360天")
    @TableField(value = "period")
    private Integer period;

    @Schema(description = "产出率")
    @TableField(value = "output_ratio")
    private BigDecimal outputRatio;

    @Schema(description = "预估总产出")
    @TableField(value = "expected_total_output")
    private BigDecimal expectedTotalOutput;

    @Schema(description = "退出日期")
    @TableField(value = "end_time")
    private LocalDate endTime;

    @Schema(description = "${column.comment}")
    @TableField(value = "create_by")
    private String createBy;

    @Schema(description = "${column.comment}")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @Schema(description = "${column.comment}")
    @TableField(value = "update_by")
    private String updateBy;

    @Schema(description = "${column.comment}")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;


}

