package com.ruoyi.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商户信息表
 *
 * @author un
 * @date 2023-11-18 15:25:00
 */
@TableName(value = "top_user")
@Data
@Schema(description = "用户表")
public class TopUser {

    @Schema(description = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "钱包地址")
    @TableField(value = "wallet")
    private String wallet;

    @Schema(description = "波场钱包地址钱包地址")
    @TableField(value = "tron_wallet")
    private String tronWallet;

    @Schema(description = "等级")
    @TableField(value = "grade")
    private Integer grade;

    @Schema(description = "邀请码")
    @TableField(value = "invited_code")
    private String invitedCode;

    @Schema(description = "邀请人的ID")
    @TableField(value = "invited_user_id")
    private Long invitedUserId;

    @Schema(description = "邀请人的邀请码")
    @TableField(value = "invited_user_code")
    private String invitedUserCode;

    @Schema(description = "${column.comment}")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @Schema(description = "${column.comment}")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @Schema(description = "btc转账地址")
    @TableField(value = "btc_transfer_address")
    private String btcTransferAddress;


}