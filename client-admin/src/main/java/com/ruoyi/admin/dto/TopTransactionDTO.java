package com.ruoyi.admin.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.admin.enums.TransactionType;
import com.ruoyi.common.BaseDTO;
import com.ruoyi.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 【请填写功能名称】对象 top_chain
 * 
 * @author ruoyi
 * @date 2024-02-03
 */
@Data
public class TopTransactionDTO extends BaseDTO
{

    /** 链类型 */
    @Schema(description = "链hash")
    private String hash;

    @Schema(description = "事务状态,0x0:未审核,0x1:审核通过,0x2:审核拒绝",examples = {"0x0","0x1","02"})
    private String status;

//    Recharge("充值"),
//    TronRecharge("波场充值"),
//    Withdraw_BTC("提现BTC"),
//    Withdraw("提现"),
//    Tron_Withdraw("波场提现");
    @Schema(description = "事务类型,Recharge:充值,TronRecharge:波场充值,Withdraw_BTC:提现BTC,Withdraw:提现,Tron_Withdraw:波场提现")
    private TransactionType type;

}
