package com.ruoyi.admin.controller;

import com.ruoyi.admin.service.TopAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(description = "资产", name = "资产")
@RestController
@RequestMapping("topAccount")
public class TopAccountController {

    @Autowired
    private TopAccountService service;


}

