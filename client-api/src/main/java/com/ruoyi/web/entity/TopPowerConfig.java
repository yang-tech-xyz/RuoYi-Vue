package com.ruoyi.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 算力配置表
 * 
 * @author ruoyi
 * @date 2024-02-03
 */
@Data
@ApiModel
public class TopPowerConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 链信息配置表 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 链类型 */
    @ApiModelProperty(name = "算力购买价格")
    private BigDecimal price;

    /** rpc 节点url */
    @ApiModelProperty(name = "算力周期")
    private Integer period;

    /** 在多少个区块确认之后 才确认充值成功 */
    @ApiModelProperty(name = "产出币种")
    private String outputSymbol;

    /** 链id */
    @ApiModelProperty(name = "产出比例")
    private BigDecimal outputRatio;


}
