package com.ruoyi.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SettleMemberCountVO {


    @Schema(description = "昨日注册用户")
    private Long yesterdayCount = 0L;

    @Schema(description = "今日注册用户")
    private Long todayCount = 0L;

    @Schema(description = "新增注册用户")
    private Long diff;

    public Long getDiff() {
        if (todayCount > yesterdayCount) {
            return todayCount - yesterdayCount;
        } else {
            return 0L;
        }
    }

}