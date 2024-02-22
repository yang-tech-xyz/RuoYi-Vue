package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.service.TopPowerDailyIncomeService;
import com.ruoyi.web.utils.RequestUtil;
import com.ruoyi.web.vo.DailyIncomeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(description = "挖矿收益", name = "挖矿收益")
@RestController
@RequestMapping("topPowerDailyIncome")
public class TopPowerDailyIncomeController {

    @Autowired
    private TopPowerDailyIncomeService service;

    @Operation(summary = "查询未领取收益")
    @GetMapping("/getUnclaimedList")
    public AjaxResult<List<DailyIncomeVO>> getUnclaimedList() {
        return AjaxResult.success(service.getUnclaimedList(RequestUtil.getWalletAddress()));
    }

    @Operation(summary = "领取收益")
    @PostMapping("/claim")
    public AjaxResult<Boolean> claim(Long id) {
        return AjaxResult.success(service.claim(RequestUtil.getWalletAddress(), id));
    }

    @Operation(summary = "领取全部收益")
    @PostMapping("/claimAll")
    public AjaxResult<Boolean> claimAll() {
        return AjaxResult.success(service.claimAll(RequestUtil.getWalletAddress()));
    }
}