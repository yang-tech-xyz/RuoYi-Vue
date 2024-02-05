package com.ruoyi.web.vo;

import com.ruoyi.common.SignBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * 用户钱包注册信息
 * @author ruoyi
 */
@ApiModel("购买订单请求")
@Data
public class BuyPowerBody extends SignBaseEntity
{
    /**
     * 用户钱包地址
     */
    @ApiModelProperty(value="使用的token",example = "SUI")
    private String symbol;

    /**
     * 用户签名后信息
     */
    @ApiModelProperty(value="购买的数量",example = "2")
    private BigDecimal number;

}
