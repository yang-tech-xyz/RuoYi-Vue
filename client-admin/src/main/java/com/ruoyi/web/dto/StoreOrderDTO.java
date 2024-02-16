package com.ruoyi.web.dto;

import com.ruoyi.common.SignBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreOrderDTO extends SignBaseEntity {

    @Schema(description = "产品ID")
    @NotNull
    private Long storeId;

    @Schema(description = "投注金额:USD")
    @NotNull
    private Integer amount;

}
