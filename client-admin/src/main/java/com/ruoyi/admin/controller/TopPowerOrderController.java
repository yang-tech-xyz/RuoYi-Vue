package com.ruoyi.admin.controller;

import com.ruoyi.admin.service.TopPowerOrderService;
import com.ruoyi.admin.service.TopUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 充值
 *
 * @author ruoyi
 */
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/power")
@Tag(description = "TopPowerOrderController", name = "订单操作")
@RestController
public class TopPowerOrderController {

    private final TopPowerOrderService topPowerOrderService;

    private final TopUserService topUserService;


}
