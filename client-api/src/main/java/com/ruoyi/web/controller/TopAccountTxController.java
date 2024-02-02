package com.ruoyi.web.controller;

import com.ruoyi.web.service.TopAccountTxService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("topAccountTx")
public class TopAccountTxController {
 
    @Autowired
    private TopAccountTxService service;

}

