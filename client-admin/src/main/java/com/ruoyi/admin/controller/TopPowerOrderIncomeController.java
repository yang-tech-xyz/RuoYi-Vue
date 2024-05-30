package com.ruoyi.admin.controller;

import com.ruoyi.admin.dto.PowerOrderIncomePageDTO;
import com.ruoyi.admin.service.TopPowerOrderIncomeService;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.admin.vo.PowerOrderIncomeStatisticsVO;
import com.ruoyi.admin.vo.PowerOrderIncomeVO;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(description = "算力收益明细", name = "算力收益明细")
@RestController
@RequestMapping("topPowerOrderIncome")
public class TopPowerOrderIncomeController {

    @Autowired
    private TopPowerOrderIncomeService service;

    @Operation(summary = "汇总")
    @GetMapping("/getStatistics")
    public AjaxResult<List<PowerOrderIncomeStatisticsVO>> getStatistics(@ModelAttribute PowerOrderIncomePageDTO dto) {
        return AjaxResult.success(service.getStatistics(dto));
    }

    @Operation(summary = "查询记录")
    @GetMapping("/getPage")
    public AjaxResult<PageVO<PowerOrderIncomeVO>> getPage(@ModelAttribute PowerOrderIncomePageDTO dto) {
        return AjaxResult.success(service.getPage(dto));
    }

}

