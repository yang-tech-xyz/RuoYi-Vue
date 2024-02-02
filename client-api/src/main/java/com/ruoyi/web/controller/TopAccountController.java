package com.ruoyi.web.controller;

import com.ruoyi.web.service.TopAccountService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("topAccount")
public class TopAccountController {
 
    @Autowired
    private TopAccountService service;

}

