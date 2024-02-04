package com.ruoyi.web.dto;

import com.ruoyi.web.enums.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {

    private String uniqueId;

    private Long mebId;

    private String token;

    private BigDecimal fee;

    private BigDecimal balanceChanged;

    private Account.Balance balanceTxType;

    private Account.TxType txType;

    private String refNo;

    private String remark;

}