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
@TableName(value = "top_store")
public class TopStore implements Serializable {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "产品名称")
    @TableField(value = "name")
    private String name;

    @Schema(description = "产品周期（月）,自然月")
    @TableField(value = "period")
    private Integer period;

    @Schema(description = "最低投资数量")
    @TableField(value = "limit_min_amount")
    private BigDecimal limitMinAmount;

    @Schema(description = "展示利率")
    @TableField(value = "display_rate")
    private String displayRate;

    @Schema(description = "实际利率")
    @TableField(value = "rate")
    private BigDecimal rate;

    @Schema(description = "状态：1=有效，2=无效")
    @TableField(value = "status")
    private Integer status;

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

