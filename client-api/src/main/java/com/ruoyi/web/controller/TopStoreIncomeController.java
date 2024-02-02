package com.ruoyi.web.controller;

import com.ruoyi.web.service.TopStoreIncomeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("topStoreIncome")
public class TopStoreIncomeController {
 
    @Autowired
    private TopStoreIncomeService service;

}

