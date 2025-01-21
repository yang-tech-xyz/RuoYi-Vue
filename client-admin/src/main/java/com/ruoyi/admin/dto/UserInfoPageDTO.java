package com.ruoyi.admin.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserInfoPageDTO {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    private Long id;

    private Long uid;

    private String username;

    private Integer status;

    private String createTime;

    private String updateTime;

}
