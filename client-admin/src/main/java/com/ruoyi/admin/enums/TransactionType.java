package com.ruoyi.admin.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {
    Recharge("充值"),
    TronRecharge("波场充值"),
    Withdraw_BTC("提现BTC"),
    Withdraw("提现"),
    Tron_Withdraw("波场提现");

    private final String description;
}
