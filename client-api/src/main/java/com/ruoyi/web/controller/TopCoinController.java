package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.service.TopTokenService;
import com.ruoyi.web.vo.RechargeBody;
import com.ruoyi.web.vo.TopTokenChainVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 充值
 * 
 * @author ruoyi
 */
@Slf4j
@RequestMapping("/token")
@Tag(description = "TokenController", name = "token信息")
@RestController
public class TopCoinController
{

    @Autowired
    private TopTokenService topTokenService;

    @Operation(summary = "根据链id查询所有支持的token")
    @GetMapping("queryTokensByChainId")
    public AjaxResult queryTokensByChainId(@Parameter String chainId){
        List<TopTokenChainVO> list= topTokenService.queryTokensByChainId(chainId);
        return AjaxResult.success("success",list);
    }


    /**
     * 充值
     *
     * @param rechargeBody 充值hash值
     * @return 结果
     */
    @Operation(summary = "充值到账")
    @PostMapping("/recharge")
    public AjaxResult recharge(@RequestBody RechargeBody rechargeBody)throws Exception{
        return topTokenService.recharge(rechargeBody);
    }


}
