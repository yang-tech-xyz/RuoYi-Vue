package com.ruoyi.web.controller;

import com.ruoyi.web.service.TopStoreService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "存币生息", value = "存币生息")
@RestController
@RequestMapping("topStore")
public class TopStoreController {

    @Autowired
    private TopStoreService service;

}

