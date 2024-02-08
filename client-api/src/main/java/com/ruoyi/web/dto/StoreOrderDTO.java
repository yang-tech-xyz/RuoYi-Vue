package com.ruoyi.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreOrderDTO {

    @Schema(description = "产品ID")
    @NotNull
    private Long storeId;

    @Schema(description = "投注金额")
    @NotNull
    private Integer amount;

}
