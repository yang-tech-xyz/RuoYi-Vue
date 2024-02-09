package com.ruoyi.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class TopUserEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 主键
     */
    @Schema(description = "钱包地址")
    private String wallet;

    @Schema(description = "等级")
    private Integer grade;

    @Schema(description = "邀请码")
    private String invitedCode;


    @Schema(description = "邀请人的邀请码")
    private String invitedUserCode;

    @Schema(description = "邀请人的ID")
    private Long invitedUserId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}