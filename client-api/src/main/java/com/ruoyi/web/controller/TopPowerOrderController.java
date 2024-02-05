package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.service.TopPowerOrderService;
import com.ruoyi.web.service.TopTokenService;
import com.ruoyi.web.utils.UnsignMessageUtils;
import com.ruoyi.web.vo.BuyPowerBody;
import com.ruoyi.web.vo.RechargeBody;
import com.ruoyi.web.vo.TopTokenChainVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.util.List;

/**
 * 充值
 * 
 * @author ruoyi
 */
@Slf4j
@RequestMapping("/power")
@Tag(description = "TopPowerOrderController", name = "token信息")
@RestController
public class TopPowerOrderController
{

    @Autowired
    private TopPowerOrderService topPowerOrderService;

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


}
