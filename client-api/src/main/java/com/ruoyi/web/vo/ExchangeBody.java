package com.ruoyi.web.vo;

import com.ruoyi.common.SignBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * 用户钱包注册信息
 * @author ruoyi
 */
@Data
@Schema(description = "内部转账")
public class ExchangeBody extends SignBaseEntity
{
    /**
     * 用户钱包地址
     */
    @Schema(description="转账数量",example = "100")
    @Positive
    private BigDecimal amount;

}
