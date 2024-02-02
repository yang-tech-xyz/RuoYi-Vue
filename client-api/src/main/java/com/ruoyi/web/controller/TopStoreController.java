package com.ruoyi.web.controller;

import com.ruoyi.web.service.TopStoreService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("topStore")
public class TopStoreController {
 
    @Autowired
    private TopStoreService service;

}

