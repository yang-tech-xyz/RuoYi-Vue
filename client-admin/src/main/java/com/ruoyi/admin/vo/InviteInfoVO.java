package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InviteInfoVO {

    @Schema(description = "等级")
    private Integer grade;

    @Schema(description = "自己的邀请码")
    private String invitedCode;

    @Schema(description = "邀请人的钱包地址")
    private String invitedUserWallet;

    @Schema(description = "全部下级投资额")
    private BigDecimal totalAmount;

    @Schema(description = "全部下级人数")
    private Integer totalInviteUser;

}
