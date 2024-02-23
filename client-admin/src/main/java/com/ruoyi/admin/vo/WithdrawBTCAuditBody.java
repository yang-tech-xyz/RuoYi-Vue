package com.ruoyi.admin.vo;

import com.ruoyi.common.SignBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * 用户钱包注册信息
 * @author ruoyi
 */
@Data
@Schema(description = "提币审核")
public class WithdrawBTCAuditBody extends SignBaseEntity
{
    @Schema(description = "是否通过,true:通过,false:拒绝")
    private Boolean pass;

    @Schema(description = "BTC提现生成的hash值")
    private String hash;

    /**
     * 币种标记
     */
    @Schema(description="事务编号", example= "c7b51ba8-bdcd-4a29-bdfb-4841216266be")
    private String transactionNo;


}
