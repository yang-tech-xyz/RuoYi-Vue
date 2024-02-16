package com.ruoyi.web.vo;

import com.ruoyi.web.entity.TopPowerOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class UserProcessVO {

    private Long id;

    private String wallet;

    @Schema(description = "邀请码")
    private String invitedCode;

    @Schema(description = "邀请人的邀请码")
    private String invitedUserCode;

    private Long invitedUserId;

    private List<TopPowerOrder> powerOrders;

    private Integer powerNumber = 0;

    @Schema(description = "合计收益币种")
    private Map<String, BigDecimal> dailyIncomeMap = new HashMap<>();

    @Schema(description = "直邀人")
    private List<UserProcessVO> directChildMebList = new ArrayList<>();

}