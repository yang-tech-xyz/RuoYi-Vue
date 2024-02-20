package com.ruoyi.admin.controller;

import com.ruoyi.admin.entity.TopPowerConfig;
import com.ruoyi.admin.entity.TopToken;
import com.ruoyi.admin.entity.TopTransaction;
import com.ruoyi.admin.exception.ServiceException;
import com.ruoyi.admin.service.TopPowerConfigService;
import com.ruoyi.admin.service.TopTokenService;
import com.ruoyi.admin.service.TopTransactionService;
import com.ruoyi.admin.utils.UnsignMessageUtils;
import com.ruoyi.admin.vo.TokenVO;
import com.ruoyi.admin.vo.WithdrawAuditBody;
import com.ruoyi.common.AjaxResult;
import com.ruoyi.common.CommonSymbols;
import com.ruoyi.web.dto.TopTokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.time.LocalDateTime;
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
public class TopTokenController {

    @Autowired
    private TopTokenService topTokenService;

    @Autowired
    private TopTransactionService topTransactionService;

    @Autowired
    private TopPowerConfigService topPowerConfigService;

    @Operation(summary = "查询当前所有币种")
    @GetMapping("/getList")
    public AjaxResult<List<TokenVO>> getList() {
        return AjaxResult.success(topTokenService.getList());
    }

    @Operation(summary = "新增token")
    @PostMapping("/")
    public AjaxResult<String> add(TopTokenDTO topTokenDTO){
        TopToken topToken = new TopToken();
        BeanUtils.copyProperties(topTokenDTO,topToken);
        topToken.setCreateTime(LocalDateTime.now());
        topToken.setUpdateTime(LocalDateTime.now());
        topToken.setCreateBy("sys");
        topToken.setUpdateBy("sys");
        topTokenService.save(topToken);
        return AjaxResult.success();
    }

    @Operation(summary = "修改token")
    @PutMapping("/")
    public AjaxResult<String> edit(TopToken topToken){
        topToken.setUpdateTime(LocalDateTime.now());
        topTokenService.updateById(topToken);
        return AjaxResult.success();
    }

    @Operation(summary = "删除token")
    @DeleteMapping("/{id}")
    public AjaxResult<String> remove(@PathVariable Long id){
        topTokenService.removeById(id);
        return AjaxResult.success();
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
            String auditWallet = topPowerConfig.getAuditWallet();
            try {
                boolean validateResult = UnsignMessageUtils.validate(withdrawBody.getSignMsg(), withdrawBody.getContent(), auditWallet);
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

            topTokenService.withdrawAuditPass(topTransaction);

        }else{
            // 提现拒绝,
            topTokenService.withdrawAuditReject(topTransaction);
        }
        return AjaxResult.success();
    }
}
