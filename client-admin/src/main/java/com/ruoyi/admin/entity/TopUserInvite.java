package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "top_user_invite")
public class TopUserInvite implements Serializable {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "邀请人ID")
    @TableField(value = "invite_user_id")
    private Long inviteUserId;

    @Schema(description = "层级")
    @TableField(value = "level")
    private Integer level;

    @Schema(description = "创建日期")
    @TableField(value = "created_date")
    private LocalDateTime createdDate;

    @Schema(description = "创建人")
    @TableField(value = "created_by")
    private String createdBy;

    @Schema(description = "更新日期")
    @TableField(value = "updated_date")
    private LocalDateTime updatedDate;

    @Schema(description = "更新人")
    @TableField(value = "updated_by")
    private String updatedBy;


}

