package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.dto.AccountTxPageDTO;
import com.ruoyi.web.service.TopAccountTxService;
import com.ruoyi.web.utils.RequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(description = "资产流水", name = "资产流水")
@RestController
@RequestMapping("topAccountTx")
public class TopAccountTxController {

    @Autowired
    private TopAccountTxService service;

    @Operation(summary="查询流水记录")
    @GetMapping("/getPage")
    public AjaxResult getPage(@ModelAttribute AccountTxPageDTO dto) {
        return AjaxResult.success(service.getPage(RequestUtil.getWallet(), dto));
    }

}

