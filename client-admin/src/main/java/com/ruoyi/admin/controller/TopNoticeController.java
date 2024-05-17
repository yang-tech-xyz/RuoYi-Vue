package com.ruoyi.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.admin.entity.TopNotice;
import com.ruoyi.admin.service.TopNoticeService;
import com.ruoyi.admin.utils.RequestUtil;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(description = "公告管理", name = "公告管理")
@RestController
@RequestMapping("topNotice")
public class TopNoticeController {

    @Autowired
    private TopNoticeService service;

    @Operation(summary = "查询记录")
    @GetMapping("/getList")
    public AjaxResult<List<TopNotice>> getList() {
        return AjaxResult.success(service.list(new LambdaQueryWrapper<TopNotice>().orderByDesc(TopNotice::getSeq)));
    }

    @Operation(summary = "新增")
    @PostMapping("/add")
    public AjaxResult<Boolean> add(@RequestBody TopNotice dto) {
        dto.setId(null);
        dto.setCreatedBy(RequestUtil.getAdminId());
        dto.setCreatedDate(LocalDateTime.now());
        dto.setUpdatedBy(RequestUtil.getAdminId());
        dto.setUpdatedDate(LocalDateTime.now());
        return AjaxResult.success(service.save(dto));
    }

    @Operation(summary = "修改")
    @PostMapping("/edit")
    public AjaxResult<Boolean> edit(@RequestBody TopNotice dto) {
        dto.setUpdatedBy(RequestUtil.getAdminId());
        dto.setUpdatedDate(LocalDateTime.now());
        return AjaxResult.success(service.updateById(dto));
    }

    @Operation(summary = "删除")
    @PostMapping("/delete/{id}")
    public AjaxResult<Boolean> delete(@PathVariable String id) {
        return AjaxResult.success(service.removeById(id));
    }
}

