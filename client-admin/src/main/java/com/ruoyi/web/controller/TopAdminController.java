package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.dto.AdminLoginDTO;
import com.ruoyi.web.service.TopAdminService;
import com.ruoyi.web.vo.AdminLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

