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
public class InternalTransferBody extends SignBaseEntity
{
    /**
     * 用户钱包地址
     */
    @Schema(description="转账数量",example = "100")
    @Positive
    private BigDecimal amount;

    /**
     * 转账的钱包地址
     */
    @Schema(description="收钱钱包地址", example= "0xb0a3f85ead5cefde865fd5978c58c15ea0e8a869")
    private String receiveAddress;

    /**
     * 币种标记
     */
    @Schema(description="币种标记", example= "SUI")
    private String symbol;


}
