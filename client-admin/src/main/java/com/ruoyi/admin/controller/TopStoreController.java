package com.ruoyi.admin.controller;

import com.ruoyi.admin.dto.StoreAddDTO;
import com.ruoyi.admin.dto.StoreUpdateDTO;
import com.ruoyi.admin.service.TopStoreService;
import com.ruoyi.admin.vo.StoreVO;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(description = "存币生息", name = "存币生息")
@RestController
@RequestMapping("topStore")
public class TopStoreController {

    @Autowired
    private TopStoreService service;

    @Operation(summary = "查询记录")
    @GetMapping("/getList")
    public AjaxResult<List<StoreVO>> getList() {
        return AjaxResult.success(service.getList());
    }

    @Operation(summary = "新增")
    @PostMapping("/add")
    public AjaxResult<Boolean> add(@RequestBody StoreAddDTO dto) {
        return AjaxResult.success(service.add(dto));
    }

    @Operation(summary = "修改")
    @PostMapping("/edit")
    public AjaxResult<Boolean> edit(@RequestBody StoreUpdateDTO dto) {
        return AjaxResult.success(service.edit(dto));
    }

}