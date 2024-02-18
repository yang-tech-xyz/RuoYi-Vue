package com.ruoyi.admin.controller;

import com.ruoyi.admin.service.TopStoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(description = "存币生息", name = "存币生息")
@RestController
@RequestMapping("topStore")
public class TopStoreController {

    @Autowired
    private TopStoreService service;

}