package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SettleDirectVO {

    private Long userId;

    private String wallet;

    @Schema(description = "等级")
    private Integer grade;

    private LocalDateTime createTime;

    @Schema(description = "伞下用户数")
    private Integer totalInviteUser;

    @Schema(description = "伞下矿机台数")
    private Integer totalInvitePowerNumber;

}
