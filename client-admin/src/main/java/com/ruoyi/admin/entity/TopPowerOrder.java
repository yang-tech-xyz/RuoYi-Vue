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

    @Schema(description = "购买币种")
    @TableField(value = "symbol")
    private String symbol;

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

    @Schema(description = "产出率,废弃")
    @TableField(value = "output_ratio")
    private BigDecimal outputRatio;

    @Schema(description = "预估总产出,废弃")
    @TableField(value = "expected_total_output")
    private BigDecimal expectedTotalOutput;

    @Schema(description = "订单日期")
    @TableField(value = "order_date")
    private LocalDate orderDate;

    @Schema(description = "退出日期")
    @TableField(value = "end_date")
    private LocalDate endDate;

    @Schema(description = "创建时间")
    @TableField(value = "create_by")
    private String createBy;

    @Schema(description = "创建人")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @Schema(description = "${column.comment}")
    @TableField(value = "update_by")
    private String updateBy;

    @Schema(description = "${column.comment}")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;


}

