package com.ruoyi.web.controller;

import com.ruoyi.web.service.TopMiningIncomeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "挖矿收益", value = "挖矿收益")
@RestController
@RequestMapping("topMiningIncome")
public class TopMiningIncomeController {

    @Autowired
    private TopMiningIncomeService service;

}

