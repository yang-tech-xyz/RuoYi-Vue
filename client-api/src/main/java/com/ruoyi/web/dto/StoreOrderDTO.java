package com.ruoyi.web.dto;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreOrderDTO {

    @ApiModelProperty("产品ID")
    @NotNull
    private Long storeId;

    @ApiModelProperty("投注金额")
    @NotNull
    private BigDecimal amount;

}
