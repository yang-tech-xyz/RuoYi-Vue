package com.ruoyi.web.dto;

import com.ruoyi.web.enums.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountTxRequestDetail {

    private String uniqueId;

    private Long accountId;

    private Long mebId;

    private String token;

    private BigDecimal fee;

    private BigDecimal balanceChanged;

    private Account.Balance balanceTxType;

    private Account.TxType txType;

    private String refNo;

    private String remark;

}