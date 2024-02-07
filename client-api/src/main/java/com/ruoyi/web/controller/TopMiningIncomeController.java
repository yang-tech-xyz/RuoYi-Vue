package com.ruoyi.web.controller;

import com.ruoyi.web.service.TopMiningIncomeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(description = "挖矿收益", name = "挖矿收益")
@RestController
@RequestMapping("topMiningIncome")
public class TopMiningIncomeController {

    @Autowired
    private TopMiningIncomeService service;

}

