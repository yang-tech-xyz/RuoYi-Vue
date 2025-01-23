package com.ruoyi.admin.controller;

import cn.hutool.core.util.RandomUtil;
import com.ruoyi.admin.dto.UserBlockDTO;
import com.ruoyi.admin.dto.UserInfoPageDTO;
import com.ruoyi.admin.dto.UserPageDTO;
import com.ruoyi.admin.service.TopUserInfoService;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.admin.vo.UserInfoVO;
import com.ruoyi.admin.vo.UserVO;
import com.ruoyi.common.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@Slf4j
@RequestMapping("/topUserInfo")
@Tag(description = "TopUserInfoController", name = "用户信息")
@RestController
public class TopUserInfoController {

    @Autowired
    private TopUserInfoService service;

    @Operation(summary = "查询记录")
    @GetMapping("/getPage")
    public AjaxResult<PageVO<UserInfoVO>> getPage(@ModelAttribute UserInfoPageDTO dto) {
        return AjaxResult.success(service.getPage(dto));
    }

}
