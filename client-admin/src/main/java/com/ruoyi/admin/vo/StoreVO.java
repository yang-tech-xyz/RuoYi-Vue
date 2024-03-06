package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StoreVO {

    private Long id;

    @Schema(description = "产品名称")
    private String name;

    @Schema(description = "产品周期（月）,自然月")
    private Integer period;

    @Schema(description = "最低投资数量")
    private BigDecimal limitMinAmount;

    @Schema(description = "展示利率")
    private String displayRate;

    @Schema(description = "实际利率")
    private BigDecimal rate;

    @Schema(description = "状态：1=有效，2=无效")
    private Integer status;

    @Schema(description = "创建日期")
    private LocalDateTime createdDate;

    @Schema(description = "更新日期")
    private LocalDateTime updatedDate;

}