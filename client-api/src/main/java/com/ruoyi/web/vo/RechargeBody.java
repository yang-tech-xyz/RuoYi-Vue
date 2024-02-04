package com.ruoyi.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * 用户钱包注册信息
 * @author ruoyi
 */
@Data
@ApiModel
public class RechargeBody
{
    /**
     * 用户钱包地址
     */
    @ApiModelProperty(value="充值hash值",example = "0xa91a4483c075e3affcf3f7018ea488b57adab17924d8e9af67fc4bfc7b409af4")
    private String hash;

    /**
     * 用户签名后信息
     */
    @ApiModelProperty(value="链id", example= "11155111")
    private Integer chainId;

    /**
     * 币种标记
     */
    @ApiModelProperty(value="币种标记", example= "SUI")
    private String tokenSymbol;

}
