package com.ruoyi.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.common.CommonStatus;
import com.ruoyi.admin.dto.TopTransactionDTO;
import com.ruoyi.admin.entity.TopPowerConfig;
import com.ruoyi.admin.entity.TopTransaction;
import com.ruoyi.admin.exception.ServiceException;
import com.ruoyi.admin.service.TopPowerConfigService;
import com.ruoyi.admin.service.TopTokenService;
import com.ruoyi.admin.service.TopTransactionService;
import com.ruoyi.admin.utils.UnsignMessageUtils;
import com.ruoyi.admin.vo.WithdrawAuditBody;
import com.ruoyi.admin.vo.WithdrawBTCAuditBody;
import com.ruoyi.common.AjaxResult;
import com.ruoyi.common.CommonSymbols;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

/**
 * 充值
 *
 * @author ruoyi
 */
@Slf4j
@RequestMapping("/transaction")
@Tag(description = "TopTransactionController", name = "事务信息")
@RestController
@AllArgsConstructor
public class TopTransactionController {


    @Autowired
    private TopTransactionService topTransactionService;

    @Autowired
    private TopPowerConfigService topPowerConfigService;

    @Autowired
    private TopTokenService topTokenService;

    @Operation(summary = "查询当前所有币种")
    @GetMapping("/")
    public AjaxResult<IPage<TopTransaction>> getTransaction(@ParameterObject TopTransactionDTO topTransaction) {
        return AjaxResult.success(topTransactionService.getTransaction(topTransaction));
    }

    @Operation(summary = "提现审批")
    @PostMapping("/withdrawAudit")
    public AjaxResult<String> withdrawAudit(@RequestBody WithdrawAuditBody withdrawBody){
        TopTransaction topTransaction = topTransactionService.getOptByTransactionNo(withdrawBody.getTransactionNo());
        if(withdrawBody.getPass()){
            if(CommonSymbols.BTC_SYMBOL.equalsIgnoreCase(topTransaction.getSymbol())){
                log.warn("BTC can not withdraw audit");
                throw new ServiceException("BTC can not withdraw audit");
            }

            TopPowerConfig topPowerConfig = topPowerConfigService.list().getFirst();
//            String auditWallet = topPowerConfig.getAuditWallet();
//            try {
//                boolean validateResult = UnsignMessageUtils.validate(withdrawBody.getSignMsg(), withdrawBody.getContent(), auditWallet);
//                if (!validateResult) {
//                    return AjaxResult.error("validate sign error!");
//                }
//            } catch (SignatureException e) {
//                log.error("签名错误", e);
//                throw new ServiceException("签名错误");
//            }
            if(StringUtils.isNotEmpty(topTransaction.getHash())){
                log.error("transaction had been audit,hash is:"+topTransaction.getHash());
                throw new ServiceException("transaction had been audit");
            }
            if(-1 == topTransaction.getChainId()){
                topTokenService.tronWithdrawAuditPass(topTransaction);
            }else{
                topTokenService.withdrawAuditPass(topTransaction);
            }
        }else{
            // 提现拒绝,
            String status = topTransaction.getStatus();
            if(!CommonStatus.STATES_COMMIT.equalsIgnoreCase(status)){
                log.error("transaction had been audit,hash is:"+topTransaction.getHash());
                throw new ServiceException("transaction had been reject");
            }
            topTokenService.withdrawAuditReject(topTransaction);
        }
        return AjaxResult.success();
    }


    @Operation(summary = "BTC提现审批")
    @PostMapping("/withdrawBTCAudit")
    public AjaxResult<String> withdrawBTCAudit(@RequestBody WithdrawBTCAuditBody withdrawBTCAuditBody){
        TopTransaction topTransaction = topTransactionService.getOptByTransactionNo(withdrawBTCAuditBody.getTransactionNo());
        if(withdrawBTCAuditBody.getPass()){
            if(!CommonSymbols.BTC_SYMBOL.equalsIgnoreCase(topTransaction.getSymbol())){
                log.warn("NOT BTC can not withdraw audit");
                throw new ServiceException("NOT BTC can not withdraw audit");
            }

            TopPowerConfig topPowerConfig = topPowerConfigService.list().getFirst();
            String auditWallet = topPowerConfig.getAuditWallet();
            try {
                boolean validateResult = UnsignMessageUtils.validate(withdrawBTCAuditBody.getSignMsg(), withdrawBTCAuditBody.getContent(), auditWallet);
                if (!validateResult) {
                    return AjaxResult.error("validate sign error!");
                }
            } catch (SignatureException e) {
                log.error("签名错误", e);
                throw new ServiceException("签名错误");
            }
            if(StringUtils.isNotEmpty(topTransaction.getHash())){
                log.error("transaction had been audit,hash is:"+topTransaction.getHash());
                throw new ServiceException("transaction had been audit");
            }
            topTransaction.setHash(withdrawBTCAuditBody.getHash());
            topTokenService.withdrawBTCAuditPass(topTransaction);
        }else{
            // 提现拒绝,
            String status = topTransaction.getStatus();
            if(!CommonStatus.STATES_COMMIT.equalsIgnoreCase(status)){
                log.error("transaction had been audit,hash is:"+topTransaction.getHash());
                throw new ServiceException("transaction had been reject");
            }
            topTokenService.withdrawAuditReject(topTransaction);
        }
        return AjaxResult.success();
    }
}
