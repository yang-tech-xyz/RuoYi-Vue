package com.ruoyi.admin.vo;

import com.ruoyi.common.SignBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户钱包注册信息
 *
 * @author ruoyi
 */
@Data
public class WalletRegisterBody extends SignBaseEntity {

    /**
     * 邀请码
     */
    @Schema(description = "邀请码", example = "111")
    private String invitedCode;

}
