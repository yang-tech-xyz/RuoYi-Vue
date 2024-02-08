package com.ruoyi.web.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InviteVO {

    private String wallet;

    private BigDecimal power;

    private LocalDateTime createTime;

}
