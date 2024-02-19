package com.ruoyi.admin.vo;

import com.ruoyi.common.SignBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * 用户钱包注册信息
 * @author ruoyi
 */
@Data
@Schema(description = "提币")
public class WithdrawBody extends SignBaseEntity
{
    /**
     * 用户钱包地址
     */
    @Schema(description="提现数量",example = "100")
    private BigDecimal amount;

    /**
     * 用户签名后信息
     */
    @Schema(description="链id", example= "11155111")
    private Long chainId;

    /**
     * 币种标记
     */
    @Schema(description="币种标记", example= "SUI")
    private String symbol;


}
