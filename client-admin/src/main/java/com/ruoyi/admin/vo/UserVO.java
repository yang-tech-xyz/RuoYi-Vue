package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {

    private Long id;

    @Schema(description = "钱包地址")
    private String wallet;

    @Schema(description = "等级")
    private Integer grade;

    @Schema(description = "邀请码")
    private String invitedCode;

    @Schema(description = "邀请人的ID")
    private Long invitedUserId;

    @Schema(description = "邀请人的地址")
    private String invitedUserWallet;

    @Schema(description = "邀请人的邀请码")
    private String invitedUserCode;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @Schema(description = "btc转账地址")
    private String btcTransferAddress;

    @Schema(description = "禁止")
    private Boolean blockEnabled;

}
