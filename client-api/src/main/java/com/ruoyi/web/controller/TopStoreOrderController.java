package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.dto.StoreOrderDTO;
import com.ruoyi.web.service.TopStoreOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "存币生息订单", value = "存币生息订单")
@RestController
@RequestMapping("topStoreOrder")
public class TopStoreOrderController {

    @Autowired
    private TopStoreOrderService service;

    /**
     * TODO:缺少用户ID
     */
    @ApiOperation("存单")
    @PostMapping("/order")
    public AjaxResult order(@RequestBody StoreOrderDTO dto) {
        return AjaxResult.success(service.order(1L, dto));
    }

}
