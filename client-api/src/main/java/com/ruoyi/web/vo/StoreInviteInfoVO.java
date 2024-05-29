package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreInviteInfoVO {

    @Schema(description = "全部推广人数")
    private Integer totalInviteUser;

    @Schema(description = "全部推广收益（USD）")
    private BigDecimal totalStoreIncome = BigDecimal.ZERO;

    @Schema(description = "团队总质押数（USD）")
    private BigDecimal totalStoreAmount = BigDecimal.ZERO;

}
