package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.service.TopTokenBurningService;
import com.ruoyi.web.vo.TokenBurningVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

