package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TokenVO {

    private Long id;

    @Schema(description = "token 名称")
    private String symbol;

    @Schema(description = "小数位")
    private Integer decimals;

    @Schema(description = "价格平台")
    private String plate;

    @Schema(description = "是否上线: 0,上线,1下线")
    private Integer online;

    @Schema(description = "展示年利率")
    private String outputAnnualInterestRate;

    @Schema(description = "实际年利率")
    private BigDecimal annualInterestRate;

    @Schema(description = "当前最新价格")
    private BigDecimal price;

    @Schema(description = "自动刷新价格")
    private Boolean autoPriceEnabled;

    @Schema(description = "是否挖矿")
    private Boolean powerEnabled;

    @Schema(description = "是否理财")
    private Boolean storeEnabled;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}