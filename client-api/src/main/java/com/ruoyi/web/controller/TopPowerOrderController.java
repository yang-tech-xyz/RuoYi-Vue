package com.ruoyi.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.entity.TopPowerOrder;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.service.TopPowerOrderService;
import com.ruoyi.web.service.TopUserService;
import com.ruoyi.web.utils.UnsignMessageUtils;
import com.ruoyi.web.vo.BuyPowerBody;
import com.ruoyi.web.vo.TopPowerOrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.util.Optional;

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
public class TopPowerOrderController
{

    private final TopPowerOrderService topPowerOrderService;

    private final TopUserService topUserService;

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
        topPowerOrderService.buyOrder(buyPowerBody);
        return AjaxResult.success("success");
    }

    @Operation(summary = "查询用户的算力订单")
    @GetMapping("getPowerOrderList")
    public AjaxResult<Page<TopPowerOrderVO>> getPowerOrderList(@ParameterObject Page page, @RequestHeader(value = "WalletAddress", defaultValue = "0x5ebacac108d665819398e5c37e12b0162d781398") String walletAddress){
        TopUserEntity topUserEntity = topUserService.getByWallet(walletAddress);
        Long userId = topUserEntity.getId();
        return AjaxResult.success(topPowerOrderService.getPowerOrderList(page,userId));
    }

}
