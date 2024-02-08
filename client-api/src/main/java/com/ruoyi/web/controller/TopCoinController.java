package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.entity.TopTransaction;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.service.TopTokenService;
import com.ruoyi.web.service.TopTransactionService;
import com.ruoyi.web.utils.UnsignMessageUtils;
import com.ruoyi.web.vo.ClaimBody;
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
import java.util.Optional;

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

    @Autowired
    private TopTransactionService topTransactionService;

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
        Optional<TopTransaction> topTransactionOptional = topTransactionService.getTransactionByHash(rechargeBody.getHash());
        if (topTransactionOptional.isPresent()) {
            return AjaxResult.error("transaction has exist!");
        }
        return topTokenService.recharge(rechargeBody);
    }

    /**
     * 提币
     */
    @Operation(summary = "提币")
    @PostMapping("/claim")
    public AjaxResult claim(@RequestBody ClaimBody claimBody)throws Exception{
        try {
            boolean validateResult = UnsignMessageUtils.validate(claimBody.getSignMsg(),claimBody.getContent(),claimBody.getWallet());
            if(!validateResult){
                return AjaxResult.error("validate sign error!");
            }
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        return topTokenService.claim(claimBody);
    }
}
