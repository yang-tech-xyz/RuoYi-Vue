package com.ruoyi.admin.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {
    Recharge("充值"),
    Withdraw_BTC("提现BTC"),
    Tron_Withdraw("波场提现"),
    Withdraw("提现");

    private final String description;
}
