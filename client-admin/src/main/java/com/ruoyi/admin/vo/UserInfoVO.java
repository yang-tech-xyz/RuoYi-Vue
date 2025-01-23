package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserInfoVO{

    private Long id;

    private Long uid;

    private String username;

    private Integer status;

    private String createTime;

    private String updateTime;

    private String walletAddress;

    @Schema(description = "参与活动次数")
    private Integer attendActiveTime;

    @Schema(description = "拉新用户数")
    private Integer invitedNewUserAmount;

    @Schema(description = "FanToken持有总数")
    private Integer fanTokenAmount;

    @Schema(description = "FanToken使用数量")
    private Integer fanTokenUseAmount;

    @Schema(description = "观看作品次数")
    private Integer watchWorkTime;

    @Schema(description = "观看作品数")
    private Integer watchWorkAmount;

}
