package com.ruoyi.admin.controller;

import com.ruoyi.admin.dto.TokenAddDTO;
import com.ruoyi.admin.dto.TokenUpdateDTO;
import com.ruoyi.admin.service.TopTokenService;
import com.ruoyi.admin.vo.TokenVO;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 充值
 */
@Slf4j
@RequestMapping("/token")
@Tag(description = "TokenController", name = "token信息")
@RestController
public class TopTokenController {

    @Autowired
    private TopTokenService service;

    @Operation(summary = "查询记录")
    @GetMapping("/getList")
    public AjaxResult<List<TokenVO>> getList() {
        return AjaxResult.success(service.getList());
    }

    @Operation(summary = "新增")
    @PostMapping("/add")
    public AjaxResult<Boolean> add(@RequestBody TokenAddDTO dto) {
        return AjaxResult.success(service.add(dto));
    }

    @Operation(summary = "修改")
    @PostMapping("/edit")
    public AjaxResult<Boolean> edit(@RequestBody TokenUpdateDTO dto) {
        return AjaxResult.success(service.edit(dto));
    }

}