package com.ruoyi.admin.controller;

import com.ruoyi.admin.dto.PowerOrderIncomePageDTO;
import com.ruoyi.admin.dto.PowerOrderSharingIncomePageDTO;
import com.ruoyi.admin.service.TopPowerSharingIncomeService;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.admin.vo.PowerOrderIncomeStatisticsVO;
import com.ruoyi.admin.vo.PowerOrderSharingIncomeStatisticsVO;
import com.ruoyi.admin.vo.PowerOrderSharingIncomeVO;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(description = "算力层级收益明细", name = "算力层级收益明细")
@RestController
@RequestMapping("topPowerSharingIncome")
public class TopPowerSharingIncomeController {

    @Autowired
    private TopPowerSharingIncomeService service;

    @Operation(summary = "汇总")
    @GetMapping("/getStatistics")
    public AjaxResult<PowerOrderSharingIncomeStatisticsVO> getStatistics(@ModelAttribute PowerOrderSharingIncomePageDTO dto) {
        return AjaxResult.success(service.getStatistics(dto));
    }

    @Operation(summary = "查询记录")
    @GetMapping("/getPage")
    public AjaxResult<PageVO<PowerOrderSharingIncomeVO>> getPage(@ModelAttribute PowerOrderSharingIncomePageDTO dto) {
        return AjaxResult.success(service.getPage(dto));
    }

}

