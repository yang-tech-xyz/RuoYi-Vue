package com.ruoyi.common.core.domain.model;

import lombok.Data;

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
    private String walletAddress;

    /**
     * 用户签名后信息
     */
    private String signMsg;

    /**
     * 邀请码
     */
    private String invitedCode;

}
