package com.ruoyi.admin.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserInfoDTO {

    private Long id;

    private Long uid;

    private String username;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
