package com.ruoyi.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 算力配置表
 * 
 * @author ruoyi
 * @date 2024-02-03
 */
@Data
public class TopPowerOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 链信息配置表 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 链类型 */
    @Schema(name = "购买金额,价格乘以购买数量等于购买金额")
    private BigDecimal amount;

    @Schema(name = "产出周期")
    private Integer period;

    /** rpc 节点url */
    @Schema(name = "购买台数")
    private BigDecimal number;

    /** 在多少个区块确认之后 才确认充值成功 */
    @Schema(name = "产出币种")
    private String outputSymbol;

    /** 链id */
    @Schema(name = "产出比例")
    private BigDecimal outputRatio;

    @Schema(name = "预计总产出")
    private BigDecimal expectedTotalOutput;

    @Schema(name = "退出时间")
    private LocalDateTime endTime;

    @Schema(name = "訂單編號")
    private String orderNo;
}
