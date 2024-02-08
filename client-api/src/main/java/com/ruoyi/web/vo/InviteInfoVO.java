package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InviteInfoVO {

    @Schema(description = "全部下级投资额")
    private BigDecimal totalAmount;

    @Schema(description = "全部下级人数")
    private Integer totalInviteUser;

    @Schema(description = "自己的邀请码")
    private String invitedCode;

    private String invitedUserWallet;

}
