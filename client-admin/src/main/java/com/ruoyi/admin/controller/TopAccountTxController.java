package com.ruoyi.admin.controller;

import com.ruoyi.admin.dto.AccountTxPageDTO;
import com.ruoyi.admin.service.TopAccountTxService;
import com.ruoyi.admin.vo.AccountTxVO;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.common.AjaxResult;
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

    @Operation(summary = "查询记录")
    @GetMapping("/getPage")
    public AjaxResult<PageVO<AccountTxVO>> getPage(@ModelAttribute AccountTxPageDTO dto) {
        return AjaxResult.success(service.getPage(dto));
    }
}

