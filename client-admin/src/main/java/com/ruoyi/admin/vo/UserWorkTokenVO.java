package com.ruoyi.admin.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UserWorkTokenVO{

    private Long id;

    private Long uid;

    private Long workId;

    private BigDecimal tokenBalance;

    private String createTime;

    private String updateTime;

}
