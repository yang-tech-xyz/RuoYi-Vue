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

        STORE_IN("STORE_IN", "理财存币"),

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