package com.ruoyi.admin.controller;

import com.ruoyi.admin.dto.AccountPageDTO;
import com.ruoyi.admin.service.TopAccountService;
import com.ruoyi.admin.vo.AccountPageVO;
import com.ruoyi.admin.vo.AccountVO;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(description = "资产", name = "资产")
@RestController
@RequestMapping("topAccount")
public class TopAccountController {

    @Autowired
    private TopAccountService service;

    @Operation(summary = "查询记录")
    @GetMapping("/getPage")
    public AjaxResult<PageVO<AccountVO>> getPage(@ModelAttribute AccountPageDTO dto) {
        return AjaxResult.success(service.getPage(dto));
    }
}