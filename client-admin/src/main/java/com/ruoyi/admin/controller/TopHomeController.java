package com.ruoyi.admin.controller;

import com.ruoyi.admin.service.TopHomeService;
import com.ruoyi.admin.vo.HomeDepositVO;
import com.ruoyi.admin.vo.HomeMemberCountVO;
import com.ruoyi.admin.vo.HomeWithdrawVO;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(description = "HOME", name = "HOME")
@RestController
@RequestMapping("topHome")
public class TopHomeController {

    @Autowired
    private TopHomeService service;

    @Operation(summary = "入金")
    @GetMapping("/getDepositList")
    public AjaxResult<List<HomeDepositVO>> getDepositList(String symbol, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return AjaxResult.success(service.getDepositList(symbol, date));
    }

    @Operation(summary = "提现")
    @GetMapping("/getWithdrawList")
    public AjaxResult<List<HomeWithdrawVO>> getWithdrawList(String symbol, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return AjaxResult.success(service.getWithdrawList(symbol, date));
    }

    @Operation(summary = "用户数")
    @GetMapping("/getMemberCountList")
    public AjaxResult<List<HomeMemberCountVO>> getMemberCountList(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return AjaxResult.success(service.getMemberCountList(date));
    }

}