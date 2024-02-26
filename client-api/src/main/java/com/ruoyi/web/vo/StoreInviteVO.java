package com.ruoyi.web.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreInviteVO {

    private Integer grade;

    private String wallet;

    private BigDecimal investAmount = BigDecimal.ZERO;

}