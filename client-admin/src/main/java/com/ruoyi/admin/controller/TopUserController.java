package com.ruoyi.admin.controller;

import com.ruoyi.admin.service.TopUserInviteService;
import com.ruoyi.admin.service.TopUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@Slf4j
@RequestMapping("/topUser")
@Tag(description = "TopUserController", name = "用户信息")
@RestController
public class TopUserController {

    @Autowired
    private TopUserService topUserService;

    @Autowired
    private TopUserInviteService inviteService;

}
