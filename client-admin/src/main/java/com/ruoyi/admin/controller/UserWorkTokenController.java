package com.ruoyi.admin.controller;

import cn.hutool.core.util.RandomUtil;
import com.ruoyi.admin.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.admin.dto.UserWorkTokenDTO;
import com.ruoyi.admin.service.UserWorkTokenService;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.admin.vo.UserWorkTokenVO;
import com.ruoyi.common.AjaxResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 *
 */
@Slf4j
@RequestMapping("/userWorkTokenList")
@Tag(description = "UserWorkTokenController", name = "用户token信息")
@RestController
public class UserWorkTokenController {

    @Autowired
    private UserWorkTokenService service;

    @Operation(summary = "查询记录")
    @GetMapping("/getPage")
    public AjaxResult<PageVO<UserWorkTokenVO>> getPage(@ModelAttribute UserWorkTokenDTO dto) {
        return AjaxResult.success(service.getPage(dto));
    }

}
