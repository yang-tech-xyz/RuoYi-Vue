package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.dto.PowerOrderPageDTO;
import com.ruoyi.web.service.TopPowerOrderService;
import com.ruoyi.web.utils.RequestUtil;
import com.ruoyi.web.utils.UnsignMessageUtils;
import com.ruoyi.web.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

/**
 * 充值
 * 
 * @author ruoyi
 */
@Slf4j
@RequestMapping("/power")
@Tag(description = "TopPowerOrderController", name = "订单操作")
@RestController
public class TopPowerOrderController
{

    @Autowired
    private TopPowerOrderService service;

    @Operation(summary = "购买算力,需要用户签名")
    @PostMapping("buyOrder")
    public AjaxResult buyOrder(@RequestBody BuyPowerBody buyPowerBody){
        try {
            boolean validateResult = UnsignMessageUtils.validate(buyPowerBody.getSignMsg(),buyPowerBody.getContent(),buyPowerBody.getWallet());
            if(!validateResult){
                return AjaxResult.error("validate sign error!");
            }
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        service.buyOrder(buyPowerBody);
        return AjaxResult.success("success");
    }

    @Operation(summary = "查询订单")
    @GetMapping("/getPage")
    public AjaxResult<PageVO<PowerOrderVO>> getPage(@ModelAttribute PowerOrderPageDTO dto) {
        return AjaxResult.success(service.getPage(RequestUtil.getWalletAddress(), dto));
    }


}
