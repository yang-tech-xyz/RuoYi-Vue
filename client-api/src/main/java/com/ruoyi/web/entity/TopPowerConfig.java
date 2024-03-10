package com.ruoyi.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 算力配置表
 * 
 * @author ruoyi
 * @date 2024-02-03
 */
@Data
public class TopPowerConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 链信息配置表 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 链类型 */
    @Schema(description = "算力购买价格")
    private BigDecimal price;

    /** rpc 节点url */
    @Schema(description = "算力周期")
    private Integer period;

    /** 在多少个区块确认之后 才确认充值成功 */
    @Schema(description = "产出币种")
    private String outputSymbol;

//    @Schema(description = "产出比例")
//    private BigDecimal outputRatio;

    @JsonIgnore
    @Schema(description = "不要问我这个是做什么的")
    private String curve;

    @Schema(description = "提现费率统一10%")
    private BigDecimal feeRatio;

    @Schema(description = "项目方波场usdt收款地址")
    private String usdtReceivedWallet;

}
