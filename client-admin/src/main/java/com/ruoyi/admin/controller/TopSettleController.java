package com.ruoyi.admin.controller;

import com.ruoyi.admin.dto.SettleDepositWithdrawPageDTO;
import com.ruoyi.admin.dto.SettleMemberInvitePageDTO;
import com.ruoyi.admin.service.TopSettleService;
import com.ruoyi.admin.vo.*;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(description = "结算管理", name = "结算管理")
@RestController
@RequestMapping("topSettle")
public class TopSettleController {

    @Autowired
    private TopSettleService service;

    @Operation(summary = "合计统计：今日新增用户数")
    @GetMapping("/getMemberCount")
    public AjaxResult<SettleMemberCountVO> getMemberCount() {
        return AjaxResult.success(service.getMemberCount());
    }

    @Operation(summary = "合计统计：今日出入金")
    @GetMapping("/getDepositWithdrawList")
    public AjaxResult<List<SettleDepositWithdrawVO>> getDepositWithdrawList() {
        return AjaxResult.success(service.getDepositWithdrawList());
    }

    @Operation(summary = "出入金统计")
    @GetMapping("/getDepositWithdrawPage")
    public AjaxResult<PageVO<SettleDepositWithdrawPageVO>> getDepositWithdrawPage(@ModelAttribute SettleDepositWithdrawPageDTO dto) {
        return AjaxResult.success(service.getDepositWithdrawPage(dto));
    }

    @Operation(summary = "用户邀请统计")
    @GetMapping("/getMemberInvitePage")
    public AjaxResult<PageVO<SettleMemberInvitePageVO>> getMemberInvitePage(@ModelAttribute SettleMemberInvitePageDTO dto) {
        return AjaxResult.success(service.getMemberInvitePage(dto));
    }

}