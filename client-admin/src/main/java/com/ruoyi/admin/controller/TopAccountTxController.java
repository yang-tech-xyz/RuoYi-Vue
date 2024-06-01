package com.ruoyi.admin.controller;

import com.ruoyi.admin.dto.AccountTxPageDTO;
import com.ruoyi.admin.dto.StoreIncomePageDTO;
import com.ruoyi.admin.service.TopAccountTxService;
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

@Tag(description = "资产流水", name = "资产流水")
@RestController
@RequestMapping("topAccountTx")
public class TopAccountTxController {

    @Autowired
    private TopAccountTxService service;

    @Operation(summary = "流水汇总")
    @GetMapping("/getStatistics")
    public AjaxResult<List<AccountTxStatisticsVO>> getStatistics(@ModelAttribute AccountTxPageDTO dto) {
        return AjaxResult.success(service.getStatistics(dto));
    }

    @Operation(summary = "查询记录")
    @GetMapping("/getPage")
    public AjaxResult<PageVO<AccountTxVO>> getPage(@ModelAttribute AccountTxPageDTO dto) {
        return AjaxResult.success(service.getPage(dto));
    }

    @Operation(summary = "理财收益汇总")
    @GetMapping("/getStoreStatistics")
    public AjaxResult<List<StoreIncomeStatisticsVO>> getStoreStatistics(@ModelAttribute StoreIncomePageDTO dto) {
        return AjaxResult.success(service.getStoreStatistics(dto));
    }

    @Operation(summary = "理财收益明细")
    @GetMapping("/getStoreIncomePage")
    public AjaxResult<PageVO<StoreIncomeVO>> getStoreIncomePage(@ModelAttribute StoreIncomePageDTO dto) {
        return AjaxResult.success(service.getStoreIncomePage(dto));
    }
}

