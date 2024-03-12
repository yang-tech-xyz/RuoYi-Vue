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
    @Schema(description="事务编号", example= "45a76c29-463b-433a-9983-78f84b71d924")
    private String transactionNo;


}
