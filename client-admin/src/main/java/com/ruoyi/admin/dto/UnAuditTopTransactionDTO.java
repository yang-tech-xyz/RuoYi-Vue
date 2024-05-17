package com.ruoyi.admin.dto;

import com.ruoyi.admin.enums.TransactionType;
import com.ruoyi.common.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 未审核提现申请订单查询DTO
 * 
 * @author ruoyi
 * @date 2024-02-03
 */
@Data
public class UnAuditTopTransactionDTO
{

    /** 链id */
    @Schema(description = "节点id")
    private Long chainId;

}
