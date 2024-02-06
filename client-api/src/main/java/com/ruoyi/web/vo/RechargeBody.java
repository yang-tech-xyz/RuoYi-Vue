package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * 用户钱包注册信息
 * @author ruoyi
 */
@Data
@Schema(description = "充值")
public class RechargeBody
{
    /**
     * 用户钱包地址
     */
    @Schema(name="充值hash值",example = "0xa91a4483c075e3affcf3f7018ea488b57adab17924d8e9af67fc4bfc7b409af4")
    private String hash;

    /**
     * 用户签名后信息
     */
    @Schema(name="链id", example= "11155111")
    private Integer chainId;

    /**
     * 币种标记
     */
    @Schema(name="币种标记", example= "SUI")
    private String tokenSymbol;

}
