package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.service.TopAccountService;
import com.ruoyi.web.utils.RequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(description = "资产", name = "资产")
@RestController
@RequestMapping("topAccount")
public class TopAccountController {

    @Autowired
    private TopAccountService service;

    @Operation(summary = "资产信息")
    @GetMapping("/getAccounts")
    public AjaxResult getAccounts(@RequestParam(required = false) String symbol) {
        return AjaxResult.success(service.getAccounts(RequestUtil.getWalletAddress(), symbol));
    }

}

