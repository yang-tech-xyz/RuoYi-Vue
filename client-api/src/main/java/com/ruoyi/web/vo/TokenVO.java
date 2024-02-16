package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TokenVO {

    @Schema(description = "token 名称")
    private String symbol;

    @Schema(description = "小数位")
    private Integer decimals;

    @Schema(description = "价格平台")
    private String plate;

    @Schema(description = "是否挖矿")
    private Boolean powerEnabled;

    @Schema(description = "是否理财")
    private Boolean storeEnabled;

    @Schema(description = "价格")
    private BigDecimal price;

}