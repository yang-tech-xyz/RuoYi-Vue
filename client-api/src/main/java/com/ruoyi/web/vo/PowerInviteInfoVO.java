package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PowerInviteInfoVO {

    @Schema(description = "自己的等级")
    private Integer grade;

    @Schema(description = "自己的邀请码")
    private String invitedCode;

    @Schema(description = "邀请人的钱包地址")
    private String invitedUserWallet;

    @Schema(description = "全部推广人数")
    private Integer totalInviteUser;

    @Schema(description = "全部推广人数矿机")
    private Integer totalInvitePowerNumber;

    @Schema(description = "全部推广收益（BTC）")
    private BigDecimal totalPowerIncome = BigDecimal.ZERO;

}