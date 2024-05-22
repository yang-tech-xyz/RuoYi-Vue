package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SettleMemberInvitePageVO {

    private Long userId;

    private String wallet;

    @Schema(description = "邀请人的ID")
    private Long invitedUserId;

    @Schema(description = "邀请人的地址")
    private String invitedUserWallet;

    @Schema(description = "被邀请人数量")
    private Long inviteMemberCount;

    @Schema(description = "被邀请人矿机台数")
    private Long inviteMemberPowerNumber;
    
}
