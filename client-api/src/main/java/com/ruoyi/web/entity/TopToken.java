package com.ruoyi.web.entity;

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
@TableName(value = "top_token")
public class TopToken implements Serializable {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "token 名称")
    @TableField(value = "symbol")
    private String symbol;

    @Schema(description = "小数位")
    @TableField(value = "decimals")
    private Integer decimals;

    @Schema(description = "价格平台")
    @TableField(value = "plate")
    private String plate;

    @Schema(description = "是否上线: 0,上线,1下线")
    @TableField(value = "online")
    private Integer online;

    @Schema(description = "是否挖矿")
    @TableField(value = "power_enabled")
    private Boolean powerEnabled;

    @Schema(description = "是否理财")
    @TableField(value = "store_enabled")
    private Boolean storeEnabled;

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

