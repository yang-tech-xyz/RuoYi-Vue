package com.ruoyi.web.controller;

import com.ruoyi.web.service.TopAccountService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "资产", value = "资产")
@RestController
@RequestMapping("topAccount")
public class TopAccountController {

    @Autowired
    private TopAccountService service;

}

