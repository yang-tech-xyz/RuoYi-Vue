package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreVO {

    @Schema(name="主键")
    private Long id;

    @Schema(name="产品名称")
    private String name;

    @Schema(name="产品周期")
    private Integer period;

    @Schema(name="存入币种")
    private String token;

    @Schema(name="最小投资额")
    private BigDecimal minOrderAmount;

    @Schema(name="最大投注额")
    private BigDecimal maxOrderAmount;

    @Schema(name="最小收益利率")
    private BigDecimal minRate;

    @Schema(name="最大收益利率")
    private BigDecimal maxRate;

}
