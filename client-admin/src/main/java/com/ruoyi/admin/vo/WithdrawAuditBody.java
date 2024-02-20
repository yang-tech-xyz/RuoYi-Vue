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
@Schema(description = "提币审核")
public class WithdrawAuditBody extends SignBaseEntity
{
    private Boolean pass;

    /**
     * 币种标记
     */
    @Schema(description="事务编号", example= "c7b51ba8-bdcd-4a29-bdfb-4841216266be")
    private String transactionNo;


}
