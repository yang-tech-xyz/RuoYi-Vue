package com.ruoyi.web.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {
    Recharge("充值"),
    Withdraw("提现");

    private final String description;
}
