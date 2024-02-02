package com.ruoyi.web.controller;

import com.ruoyi.web.service.TopStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("topStore")
public class TopStoreController {

    @Autowired
    private TopStoreService service;

}

