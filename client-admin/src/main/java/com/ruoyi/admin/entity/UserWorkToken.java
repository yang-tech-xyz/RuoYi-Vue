package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商户信息表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "user_work_token")
public class UserWorkToken {

    @Schema(description = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    @TableField(value = "uid")
    private Long uid;

    @Schema(description = "作品id")
    @TableField(value = "work_id")
    private Long workId;

    @Schema(description = "token余额")
    @TableField(value = "token_balance")
    private BigDecimal tokenBalance;

    @Schema(description = "${column.comment}")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @Schema(description = "${column.comment}")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

}