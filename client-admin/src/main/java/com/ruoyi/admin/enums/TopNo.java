package com.ruoyi.admin.enums;

public enum TopNo {

    STORE_NO("STORE_NO_", 1),
    POWER_NO("POWER_NO_", 2),
    PROCESS_NO("PROCESS_NO_", 9),


    ;
    public final String _code;
    public final Integer _workId;

    TopNo(String code, Integer workId) {
        this._code = code;
        this._workId = workId;
    }
}
