package com.ruoyi.web.controller;

import cn.hutool.cron.timingwheel.TimerTask;
import com.ruoyi.common.AjaxResult;
import com.ruoyi.common.CommonSymbols;
import com.ruoyi.web.common.CommonStatus;
import com.ruoyi.web.entity.TopTransaction;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.service.TopTokenService;
import com.ruoyi.web.service.TopTransactionService;
import com.ruoyi.web.utils.UnsignMessageUtils;
import com.ruoyi.web.vo.*;
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
public class TopTokenController {

    @Autowired
    private TopTokenService topTokenService;

    @Autowired
    private TopTransactionService topTransactionService;

    @Operation(summary = "查询当前所有币种")
    @GetMapping("/getList")
    public AjaxResult<List<TokenVO>> getList() {
        return AjaxResult.success(topTokenService.getList());
    }

    @Operation(summary = "根据链id查询所有支持的token")
    @GetMapping("queryTokensByChainId")
    public AjaxResult queryTokensByChainId(@Parameter(description = "链id",example = "11155111") String chainId) {
        List<TopTokenChainVO> list = topTokenService.queryTokensByChainId(chainId);
        return AjaxResult.success("success", list);
    }


    /**
     * 充值
     *
     * @param rechargeBody 充值hash值
     * @return 结果
     */
    @Operation(summary = "充值到账,兼容波场USDT")
    @PostMapping("/recharge")
    public AjaxResult recharge(@RequestBody RechargeBody rechargeBody) throws Exception {
        Optional<TopTransaction> topTransactionOptional = topTransactionService.getTransactionByHash(rechargeBody.getHash());
        if (topTransactionOptional.isPresent()) {
            return AjaxResult.error("transaction has exist!");
        }
        Long chainId = rechargeBody.getChainId();
        if(chainId== CommonStatus.TRON_CHAIN_ID){
            //波场链充值
            topTokenService.rechargeTRX(rechargeBody);
            try{
                TopTokenService.systemTimer.addTask(new TimerTask(() -> topTokenService.confirmTronRechargeToken(rechargeBody.getHash()), 10000));
            }catch (Exception e){
                log.error("确认充值异常",e);
            }
            return AjaxResult.success();
        }else{
            topTokenService.recharge(rechargeBody);
            try{
                TopTokenService.systemTimer.addTask(new TimerTask(() -> topTokenService.confirmRechargeToken(rechargeBody.getHash()), 10000));
            }catch (Exception e){
                log.error("确认充值异常",e);
            }
            return AjaxResult.success();
        }
    }


    /**
     * 提币申请.
     */
    @Operation(summary = "提币")
    @PostMapping("/withdraw")
    public AjaxResult claim(@RequestBody WithdrawBody withdrawBody) throws Exception {
        try {
            boolean validateResult = UnsignMessageUtils.validate(withdrawBody.getSignMsg(), withdrawBody.getContent(), withdrawBody.getWallet());
            if (!validateResult) {
                return AjaxResult.error("validate sign error!");
            }
        } catch (SignatureException e) {
            log.error("签名错误", e);
            throw new ServiceException("签名错误");
        }
        String symbol = withdrawBody.getSymbol();
        Long chainId = withdrawBody.getChainId();
        if(chainId==-1){
            topTokenService.withdrawTron(withdrawBody);
            return AjaxResult.success();
        }
        if (symbol.equalsIgnoreCase(CommonSymbols.BTC_SYMBOL)) {
            topTokenService.withdrawBTC(withdrawBody);
            return AjaxResult.success();
        } else {
            topTokenService.withdraw(withdrawBody);
            return AjaxResult.success();
        }
    }

    @Operation(summary = "内部转账")
    @PostMapping("internalTransfer")
    public AjaxResult internalTransfer(@RequestBody InternalTransferBody internalTransferBody) {
        try {
            boolean validateResult = UnsignMessageUtils.validate(internalTransferBody.getSignMsg(), internalTransferBody.getContent(), internalTransferBody.getWallet());
        } catch (SignatureException e) {
            log.error("签名错误", e);
            throw new ServiceException("签名错误");
        }
        topTokenService.internalTransferBody(internalTransferBody);
        return AjaxResult.success("Success");
    }

    @Operation(summary = "BTC市价兑换USDT")
    @PostMapping("exchangeBTC2USDT")
    public AjaxResult exchangeBTC2USDT(@RequestBody ExchangeBody exchangeBody) {
        try {
            boolean validateResult = UnsignMessageUtils.validate(exchangeBody.getSignMsg(), exchangeBody.getContent(), exchangeBody.getWallet());
        } catch (SignatureException e) {
            log.error("签名错误", e);
            throw new ServiceException("签名错误");
        }
        topTokenService.exchangeBTC2USDT(exchangeBody);
        return AjaxResult.success("Success");
    }

    @Operation(summary = "USDT兑换BTCF")
    @PostMapping("exchangeUsdt2BTCF")
    public AjaxResult exchangeUsdt2BTCF(@RequestBody ExchangeBody exchangeBody){
        try {
            boolean validateResult = UnsignMessageUtils.validate(exchangeBody.getSignMsg(), exchangeBody.getContent(), exchangeBody.getWallet());
        } catch (SignatureException e) {
            log.error("签名错误", e);
            throw new ServiceException("签名错误");
        }
        topTokenService.exchangeUsdt2BTCF(exchangeBody);
        return AjaxResult.success("Success");
    }

    @Operation(summary = "BTC兑换BTCF")
    @PostMapping("exchangeBTC2BTCF")
    public AjaxResult exchangeBTC2BTCF(@RequestBody ExchangeBody exchangeBody){
        try {
            boolean validateResult = UnsignMessageUtils.validate(exchangeBody.getSignMsg(), exchangeBody.getContent(), exchangeBody.getWallet());
        } catch (SignatureException e) {
            log.error("签名错误", e);
            throw new ServiceException("签名错误");
        }
        topTokenService.exchangeBTC2BTCF(exchangeBody);
        return AjaxResult.success("Success");
    }
}
