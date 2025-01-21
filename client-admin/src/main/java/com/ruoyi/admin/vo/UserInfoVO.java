package com.ruoyi.admin.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserInfoVO{

    private Long id;

    private Long uid;

    private String username;

    private Integer status;

    private String createTime;

    private String updateTime;

    private String wallet;


}
