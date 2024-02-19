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
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "top_token_chain")
public class TopTokenChain implements Serializable {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "token id")
    private Long tokenId;

    @Schema(description = "链id")
    private Long chainId;

    @Schema(description = "价格平台")
    private String erc20Address;

    @Schema(description = "${column.comment}")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @Schema(description = "${column.comment}")
    @TableField(value = "create_by")
    private String createBy;

    @Schema(description = "${column.comment}")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @Schema(description = "${column.comment}")
    @TableField(value = "update_by")
    private String updateBy;


}

