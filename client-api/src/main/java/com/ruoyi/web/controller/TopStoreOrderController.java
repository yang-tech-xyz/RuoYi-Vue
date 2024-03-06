package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.dto.StoreCpOrderDTO;
import com.ruoyi.web.dto.StoreOrderDTO;
import com.ruoyi.web.dto.StoreOrderPageDTO;
import com.ruoyi.web.service.TopStoreOrderService;
import com.ruoyi.web.utils.RequestUtil;
import com.ruoyi.web.utils.UnsignMessageUtils;
import com.ruoyi.web.vo.StoreOrderInfoVO;
import com.ruoyi.web.vo.PageVO;
import com.ruoyi.web.vo.StoreOrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

@Tag(description = "理财订单", name = "理财订单")
@RestController
@RequestMapping("topStoreOrder")
public class TopStoreOrderController {

    @Autowired
    private TopStoreOrderService service;

    @Operation(summary = "理财统计数据")
    @GetMapping("/orderInfo")
    public AjaxResult<StoreOrderInfoVO> orderInfo() {
        return AjaxResult.success(service.getOderInfo(RequestUtil.getWallet()));
    }

    @Operation(summary = "存单")
    @PostMapping("/order")
    public AjaxResult order(@RequestBody StoreOrderDTO dto) {
        try {
            boolean validateResult = UnsignMessageUtils.validate(dto.getSignMsg(), dto.getContent(), dto.getWallet());
            if (!validateResult) {
                return AjaxResult.error("validate sign error!");
            }
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(service.order(RequestUtil.getWallet(), dto));
    }

    @Operation(summary = "订单复投")
    @PostMapping("/cpOrder")
    public AjaxResult cpOrder(@RequestBody StoreCpOrderDTO dto) {
        try {
            boolean validateResult = UnsignMessageUtils.validate(dto.getSignMsg(), dto.getContent(), dto.getWallet());
            if (!validateResult) {
                return AjaxResult.error("validate sign error!");
            }
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(service.cpOrder(RequestUtil.getWallet(), dto));
    }

    @Operation(summary = "查询订单")
    @GetMapping("/getPage")
    public AjaxResult<PageVO<StoreOrderVO>> getPage(@ModelAttribute StoreOrderPageDTO dto) {
        return AjaxResult.success(service.getPage(RequestUtil.getWallet(), dto));
    }

}
