package com.ruoyi.admin.controller;

import com.ruoyi.admin.dto.TokenBurningDTO;
import com.ruoyi.admin.service.TopTokenBurningService;
import com.ruoyi.admin.vo.TokenBurningVO;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(description = "币种销毁管理", name = "币种销毁管理")
@RestController
@RequestMapping("topTokenBurning")
public class TopTokenBurningController {

    @Autowired
    private TopTokenBurningService service;


    @Operation(summary = "销毁数据")
    @GetMapping("/getBurning")
    public AjaxResult<TokenBurningVO> getBurning() {
        return AjaxResult.success(service.getBurning());
    }

    @Operation(summary = "提交销毁")
    @PostMapping("/burning")
    public AjaxResult<Boolean> burning(@RequestBody TokenBurningDTO dto) {
        return AjaxResult.success(service.burning(dto));
    }
}

