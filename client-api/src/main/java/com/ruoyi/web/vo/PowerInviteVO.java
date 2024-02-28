package com.ruoyi.web.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PowerInviteVO {

    private Integer grade;

    private String wallet;

    private BigDecimal power;

    private LocalDate createTime;

}
