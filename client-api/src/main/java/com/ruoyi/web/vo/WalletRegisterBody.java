package com.ruoyi.web.vo;

import com.ruoyi.common.SignBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * 用户钱包注册信息
 * @author ruoyi
 */
@Data
public class WalletRegisterBody extends SignBaseEntity
{

    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀請碼")
    private String invitedCode;

}
