package com.ruoyi.admin.controller;

import com.ruoyi.admin.dto.UserPageDTO;
import com.ruoyi.admin.service.TopUserService;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.admin.vo.UserVO;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@Slf4j
@RequestMapping("/topUser")
@Tag(description = "TopUserController", name = "用户信息")
@RestController
public class TopUserController {

    @Autowired
    private TopUserService service;

    @Operation(summary = "查询记录")
    @GetMapping("/getPage")
    public AjaxResult<PageVO<UserVO>> getPage(@ModelAttribute UserPageDTO dto) {
        return AjaxResult.success(service.getPage(dto));
    }

}
