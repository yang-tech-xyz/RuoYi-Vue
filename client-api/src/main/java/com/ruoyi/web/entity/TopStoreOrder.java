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
@TableName(value = "top_store_order")
public class TopStoreOrder implements Serializable {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "产品ID")
    @TableField(value = "store_id")
    private Long storeId;

    @Schema(description = "用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "存单号")
    @TableField(value = "order_no")
    private String orderNo;

    @Schema(description = "存入币种")
    @TableField(value = "symbol")
    private String symbol;

    @Schema(description = "币种价格")
    @TableField(value = "price")
    private BigDecimal price;

    @Schema(description = "存入数量")
    @TableField(value = "amount")
    private BigDecimal amount;

    @Schema(description = "投资金额USD：币种数量*币种价格")
    @TableField(value = "invest_amount")
    private BigDecimal investAmount;

    @Schema(description = "订单时间")
    @TableField(value = "order_date")
    private LocalDate orderDate;

    @Schema(description = "到期释放时间：订单时间+周期")
    @TableField(value = "release_date")
    private LocalDate releaseDate;

    @Schema(description = "日期天数")
    @TableField(value = "days")
    private Integer days;

    @Schema(description = "状态：1=收益中，2=已领取")
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

