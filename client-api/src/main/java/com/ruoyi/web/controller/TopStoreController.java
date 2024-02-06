package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.service.TopStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(description = "存币生息", name = "存币生息")
@RestController
@RequestMapping("topStore")
public class TopStoreController {

    @Autowired
    private TopStoreService service;

    @Operation(summary="获取有效产品")
    @GetMapping("/getList")
    public AjaxResult getList() {
        return AjaxResult.success(service.getList());
    }

}