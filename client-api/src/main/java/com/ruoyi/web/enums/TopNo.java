package com.ruoyi.web.enums;

public enum TopNo {

    STORE_NO("STORE_NO_", 1),


    ;
    public final String _code;
    public final Integer _workId;

    TopNo(String code, Integer workId) {
        this._code = code;
        this._workId = workId;
    }
}
