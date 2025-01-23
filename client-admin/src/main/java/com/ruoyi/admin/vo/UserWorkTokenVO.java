package com.ruoyi.admin.vo;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserWorkTokenVO{

    private Long id;

    private Long workId;


    @Schema(description = "推广人数")
    private Integer invitedAmount;

    private BigDecimal tokenBalance;

    private String walletAddress;

    private String createTime;

    private String updateTime;

}
