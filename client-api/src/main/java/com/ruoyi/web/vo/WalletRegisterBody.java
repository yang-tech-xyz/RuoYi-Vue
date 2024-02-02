package com.ruoyi.web.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * 用户钱包注册信息
 * @author ruoyi
 */
@Data
public class WalletRegisterBody
{
    /**
     * 用户钱包地址
     */
    @NotNull
    @Schema(description="钱包地址",defaultValue = "0x5ebacac108d665819398e5c37e12b0162d781398")
    private String walletAddress;

    /**
     * 用户签名后信息
     */
    @NotNull
    @Schema(description="簽名信息",defaultValue = "0x4a2164fff41e784af1482cd91f8b7b60f8e1216c9afc89689f83bcaa27115e4d124af62bb2f24013a55604fe37e8401bf2f607ade2b84f2d353807f3f961ac0d1c")
    private String signMsg;

    /**
     * 邀请码
     */
    @NotNull
    @Schema(description = "邀請碼")
    private String invitedCode;

}
