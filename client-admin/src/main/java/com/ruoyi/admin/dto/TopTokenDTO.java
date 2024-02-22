package com.ruoyi.web.dto;

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
@TableName(value = "top_token")
public class TopTokenDTO implements Serializable {

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


}

