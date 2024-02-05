package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.dto.StoreOrderDTO;
import com.ruoyi.web.dto.StoreOrderPageDTO;
import com.ruoyi.web.service.TopStoreOrderService;
import com.ruoyi.web.utils.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return AjaxResult.success(service.order(RequestUtil.getWalletAddress(), dto));
    }

    @ApiOperation("赎回:[到期可以赎回]")
    @PostMapping("/redeem")
    public AjaxResult redeem() {
        return AjaxResult.success(service.redeem(RequestUtil.getWalletAddress()));
    }

    @ApiOperation("查询订单")
    @GetMapping("/getPage")
    public AjaxResult getPage(@ModelAttribute StoreOrderPageDTO dto) {
        return AjaxResult.success(service.getPage(RequestUtil.getWalletAddress(), dto));
    }

}
