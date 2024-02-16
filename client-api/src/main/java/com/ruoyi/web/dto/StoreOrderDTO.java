package com.ruoyi.web.dto;

import com.ruoyi.common.SignBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreOrderDTO extends SignBaseEntity {

    @Schema(description = "产品ID")
    @NotNull
    private Long storeId;

    @Schema(description = "币种")
    @NotBlank
    private String symbol;

    @Schema(description = "投入币种数量")
    @NotNull
    @Positive
    private BigDecimal amount;

}
