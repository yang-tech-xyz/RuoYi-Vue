package com.ruoyi.admin.controller;

import com.ruoyi.admin.service.TopSettleService;
import com.ruoyi.admin.vo.SettleStatisticsVO;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Tag(description = "结算管理", name = "结算管理")
@RestController
@RequestMapping("topSettle")
public class TopSettleController {

    @Autowired
    private TopSettleService service;

    @Operation(summary = "结算统计")
    @GetMapping("/getStatistics")
    public AjaxResult<List<SettleStatisticsVO>> getStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end) {
        return AjaxResult.success(service.getStatistics(start, end));
    }

    /*@Operation(summary = "出入金详情")
    @GetMapping("/getDepositAndWithdraw")
    public AjaxResult<List<SettleStatisticsVO>> getDepositAndWithdraw(@ModelAttribute SettleDepositWithdrawDTO dto) {
        return AjaxResult.success(service.getDepositAndWithdraw(start, end));
    }*/

}