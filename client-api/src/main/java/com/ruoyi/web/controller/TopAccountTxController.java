package com.ruoyi.web.controller;

import com.ruoyi.web.service.TopAccountTxService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "资产流水", value = "资产流水")
@RestController
@RequestMapping("topAccountTx")
public class TopAccountTxController {

    @Autowired
    private TopAccountTxService service;

}

