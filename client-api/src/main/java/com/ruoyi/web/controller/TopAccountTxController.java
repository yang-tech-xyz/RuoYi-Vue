package com.ruoyi.web.controller;

import com.ruoyi.web.service.TopAccountTxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("topAccountTx")
public class TopAccountTxController {

    @Autowired
    private TopAccountTxService service;

}

