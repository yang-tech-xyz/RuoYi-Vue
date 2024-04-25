package com.ruoyi.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.entity.TopUser;
import com.ruoyi.web.service.TopPowerOrderService;
import com.ruoyi.web.service.TopUserService;
import com.ruoyi.web.utils.RequestUtil;
import com.ruoyi.web.utils.UnsignMessageUtils;
import com.ruoyi.web.vo.BuyPowerBody;
import com.ruoyi.web.vo.PowerOrderInfoVO;
import com.ruoyi.web.vo.TopPowerOrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

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

    @Operation(summary = "挖矿统计数据")
    @GetMapping("/orderInfo")
    public AjaxResult<PowerOrderInfoVO> orderInfo() {
        return AjaxResult.success(topPowerOrderService.getOderInfo(RequestUtil.getWallet()));
    }

    @Operation(summary = "购买算力,需要用户签名")
    @PostMapping("buyOrder")
    public AjaxResult<String> buyOrder(@Valid @RequestBody BuyPowerBody buyPowerBody){
        try {
            boolean validateResult = UnsignMessageUtils.validate(buyPowerBody.getSignMsg(),buyPowerBody.getContent(),buyPowerBody.getWallet());
            if(!validateResult){
                return AjaxResult.error("validate sign error!");
            }
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
//        if(buyPowerBody.getNumber()<=0){
//            return AjaxResult.error("the mount is not position");
//        }
        topPowerOrderService.buyOrder(buyPowerBody);
        return AjaxResult.success("success");
    }

    @Operation(summary = "查询用户的算力订单")
    @GetMapping("getPowerOrderList")
    public AjaxResult<Page<TopPowerOrderVO>> getPowerOrderList(@ParameterObject Page page, @RequestHeader(value = "WalletAddress", defaultValue = "0x5ebacac108d665819398e5c37e12b0162d781398") String walletAddress){
        TopUser topUserEntity = topUserService.getByWallet(walletAddress);
        Long userId = topUserEntity.getId();
        return AjaxResult.success(topPowerOrderService.getPowerOrderList(page,userId));
    }

}
