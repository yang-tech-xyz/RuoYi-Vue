package com.ruoyi.admin.controller;

import com.ruoyi.admin.dto.AdminLoginDTO;
import com.ruoyi.admin.vo.AdminLoginVO;
import com.ruoyi.common.AjaxResult;
import com.ruoyi.admin.dto.AdminAddDTO;
import com.ruoyi.admin.dto.AdminUpdateDTO;
import com.ruoyi.admin.service.TopAdminService;
import com.ruoyi.admin.vo.AdminVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(description = "管理员", name = "管理员")
@RestController
@RequestMapping("topAdmin")
public class TopAdminController {

    @Autowired
    private TopAdminService service;

    @Operation(summary = "登录")
    @PostMapping("/public/login")
    public AjaxResult<AdminLoginVO> login(@RequestBody AdminLoginDTO dto) {
        return AjaxResult.success(service.login(dto));
    }

    @Operation(summary = "新增")
    @PostMapping("/add")
    public AjaxResult<Boolean> add(@RequestBody AdminAddDTO dto) {
        return AjaxResult.success(service.add(dto));
    }

    @Operation(summary = "修改")
    @PostMapping("/edit")
    public AjaxResult<Boolean> edit(@RequestBody AdminUpdateDTO dto) {
        return AjaxResult.success(service.edit(dto));
    }

    @Operation(summary = "获取谷歌密钥")
    @GetMapping("/getGoogleSecret")
    public AjaxResult<String> getGoogleSecret(@RequestParam String account) {
        return AjaxResult.success(service.getGoogleSecret(account));
    }

    @Operation(summary = "查询所有")
    @GetMapping("/getList")
    public AjaxResult<List<AdminVO>> getList() {
        return AjaxResult.success(service.getList());
    }
}

