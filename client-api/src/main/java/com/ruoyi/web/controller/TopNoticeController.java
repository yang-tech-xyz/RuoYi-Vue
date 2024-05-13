package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.entity.TopNotice;
import com.ruoyi.web.service.TopNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(description = "资产流水", name = "资产流水")
@RestController
@RequestMapping("topNotice")
public class TopNoticeController {

    @Autowired
    private TopNoticeService service;

    @Operation(summary = "查询所有")
    @GetMapping("/getList")
    public AjaxResult<List<TopNotice>> getList() {
        return AjaxResult.success(service.getList());
    }
}
