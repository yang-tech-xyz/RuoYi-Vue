package com.ruoyi.admin.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InviteVO {

    private Integer level;

    private String wallet;

    private BigDecimal power;

    private LocalDateTime createTime;

}
