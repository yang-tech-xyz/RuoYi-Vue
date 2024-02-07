package com.ruoyi.web.vo;

import com.ruoyi.web.entity.TopPowerOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UserVO {

    private Long id;

    private String wallet;

    @Schema(description = "邀请码")
    private String invitedCode;

    @Schema(description = "邀请人的邀请码")
    private String invitedUserCode;

    private Long invitedUserId;

    private List<TopPowerOrder> powerOrders;

}