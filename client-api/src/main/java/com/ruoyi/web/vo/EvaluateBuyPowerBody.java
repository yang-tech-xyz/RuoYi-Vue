package com.ruoyi.web.vo;

import com.ruoyi.common.SignBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 *
 * 用户钱包注册信息
 * @author ruoyi
 */
@Schema(description = "购买订单请求")
@Data
public class EvaluateBuyPowerBody{
    /**
     * 用户钱包地址
     */
    @Schema(description="使用的token",example = "BTCF")
    @NotBlank
    private String symbol;

}
