package com.ruoyi.admin.dto;

import java.math.BigDecimal;

import com.ruoyi.common.BaseDTO;

import lombok.Data;

@Data
public class UserWorkTokenDTO extends BaseDTO{

    private Long id;

    private Long uid;

    private Long workId;

    private BigDecimal tokenBalance;

    private String createTime;

    private String updateTime;

}
