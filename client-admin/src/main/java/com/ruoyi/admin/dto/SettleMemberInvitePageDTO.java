package com.ruoyi.admin.dto;

import lombok.Data;

@Data
public class SettleMemberInvitePageDTO {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    private Long userId;

    private String wallet;

}
