package com.ruoyi.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreOrderDTO {

    @Schema(name="产品ID")
    @NotNull
    private Long storeId;

    @Schema(name="投注金额")
    @NotNull
    private BigDecimal amount;

}
