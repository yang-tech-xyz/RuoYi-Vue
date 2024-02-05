package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.dto.AccountTxPageDTO;
import com.ruoyi.web.service.TopAccountTxService;
import com.ruoyi.web.utils.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "资产流水", value = "资产流水")
@RestController
@RequestMapping("topAccountTx")
public class TopAccountTxController {

    @Autowired
    private TopAccountTxService service;

    @ApiOperation("查询流水记录")
    @GetMapping("/getPage")
    public AjaxResult getPage(AccountTxPageDTO dto) {
        return AjaxResult.success(service.getPage(RequestUtil.getWalletAddress(), dto));
    }

}

