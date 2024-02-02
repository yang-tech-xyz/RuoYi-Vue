package com.ruoyi.web.controller;

import com.ruoyi.web.service.TopStoreIncomeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "存币利息记录", value = "存币利息记录")
@RestController
@RequestMapping("topStoreIncome")
public class TopStoreIncomeController {

    @Autowired
    private TopStoreIncomeService service;


}

