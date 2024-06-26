package com.ruoyi.web.enums;

import org.apache.commons.lang3.StringUtils;

public interface Account {

    enum Balance {
        AVAILABLE("AVAILABLE", "可用"),
        LOCKUP("LOCKUP", "锁仓"),
        FROZEN("FROZEN", "冻结"),
        ;
        public final String typeCode;

        public final String typeName;

        Balance(String value, String msg) {
            this.typeCode = value;
            this.typeName = msg;
        }

        public static Balance getType(String typeCode) {
            for (Balance value : values()) {
                if (StringUtils.equalsIgnoreCase(value.typeCode, typeCode)) {
                    return value;
                }
            }
            return null;
        }
    }

    enum TxType {

        STORE_IN("STORE_IN", "理财"),
        PURCHASE("PURCHASE", "支付"),
        INTERNAL_TRANSFER("INTERNAL_TRANSFER", "内部转账"),
        EXCHANGE("EXCHANGE", "交易"),
        RECHARGE_IN("RECHARGE_IN", "充值"),
        TRON_RECHARGE_IN("TRON_RECHARGE_IN", "TRON充值"),
        WITHDRAW("WITHDRAW", "提现"),
        WITHDRAW_FEE("WITHDRAW_FEE", "提现手续费"),
        TRON_WITHDRAW("TRON_WITHDRAW", "波场提现"),
        WITHDRAW_REJECT("WITHDRAW_REJECT", "提现拒绝"),
        WITHDRAW_BTC("WITHDRAW_BTC", "提现BTC"),
        STORE_REDEEM("STORE_REDEEM", "理财赎回"),
        STORE_INTEREST("STORE_INTEREST", "理财利息"),
        STORE_INTEREST_INVITE("STORE_INTEREST_INVITE", "理财邀请收益"),
        POWER_DAILY_INCOME("POWER_DAILY_INCOME", "挖矿收益"),
        POWER_SHARING_INCOME("POWER_SHARING_INCOME", "挖矿邀请收益"),

        ;
        public final String typeCode;

        public final String typeName;

        TxType(String typeCode, String typeName) {
            this.typeCode = typeCode;
            this.typeName = typeName;
        }

        public static TxType getType(String typeCode) {
            for (TxType value : values()) {
                if (StringUtils.equalsIgnoreCase(value.typeCode, typeCode)) {
                    return value;
                }
            }
            return null;
        }

    }

}