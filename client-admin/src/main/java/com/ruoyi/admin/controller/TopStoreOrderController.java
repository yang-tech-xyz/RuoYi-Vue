package com.ruoyi.admin.controller;

import com.ruoyi.admin.service.TopStoreOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(description = "存币生息订单", name = "存币生息订单")
@RestController
@RequestMapping("topStoreOrder")
public class TopStoreOrderController {

    @Autowired
    private TopStoreOrderService service;

}
