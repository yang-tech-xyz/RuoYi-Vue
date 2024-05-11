package com.ruoyi.admin.common;

public class CommonStatus {

    // 审核拒绝
    public static String STATES_REJECT = "0x2";
    // 审批通过
    public static String STATES_SUCCESS = "0x1";
    // 未审核
    public static String STATES_COMMIT = "0x0";

    /**
     * 未确认状态
     */
    public static Integer UN_CONFIRM = 0;

    /**
     * 已确认状态
     */
    public static Integer IS_CONFIRM = 1;

    /**
     * 提现射核拒绝状态
     */
    public static Integer REJECT = 2;
}
