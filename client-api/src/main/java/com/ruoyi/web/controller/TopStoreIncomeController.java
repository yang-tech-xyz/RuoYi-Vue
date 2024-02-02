package com.ruoyi.web.controller;

import com.ruoyi.web.service.TopStoreIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("topStoreIncome")
public class TopStoreIncomeController {

    @Autowired
    private TopStoreIncomeService service;

}

