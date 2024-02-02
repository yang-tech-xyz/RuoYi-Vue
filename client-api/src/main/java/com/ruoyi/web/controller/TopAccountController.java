package com.ruoyi.web.controller;

import com.ruoyi.web.service.TopAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("topAccount")
public class TopAccountController {

    @Autowired
    private TopAccountService service;

}

