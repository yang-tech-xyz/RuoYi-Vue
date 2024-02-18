package com.ruoyi.admin.controller;

import com.ruoyi.admin.service.TopAccountTxService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(description = "资产流水", name = "资产流水")
@RestController
@RequestMapping("topAccountTx")
public class TopAccountTxController {

    @Autowired
    private TopAccountTxService service;

}

