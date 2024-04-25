package com.ruoyi.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
    @Schema(description="充值hash值",example = "288f102ad368eac132e61f2cb68ea70920ccfa7845e9e3e34b26321c3185b382")
    @NotNull
    private String hash;

    /**
     * 链id,TRON定义为0号链
     */
    @Schema(description="链id", example= "56")
    @NotNull
    private Long chainId;

    /**
     * 币种标记
     */
    @Schema(description="币种标记", example= "USDT")
    @NotNull
    private String tokenSymbol;

}
