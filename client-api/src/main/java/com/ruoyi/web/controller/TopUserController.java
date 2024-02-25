package com.ruoyi.web.controller;

import cn.hutool.core.util.IdUtil;
import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.entity.TopUser;
import com.ruoyi.web.enums.TopNo;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.service.*;
import com.ruoyi.web.utils.NumbersUtils;
import com.ruoyi.web.utils.RequestUtil;
import com.ruoyi.web.utils.UnsignMessageUtils;
import com.ruoyi.web.vo.BTCAddressBody;
import com.ruoyi.web.vo.WalletRegisterBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@Slf4j
@RequestMapping("/topUser")
@Tag(description = "TopUserController", name = "用户信息")
@RestController
public class TopUserController {

    @Autowired
    private TopUserService topUserService;

    @Autowired
    private TopUserInviteService inviteService;

    @Operation(summary = "用户查询", description = "用户查询")
    @GetMapping("")
    public AjaxResult<TopUser> queryUser(@RequestHeader(value = "WalletAddress", defaultValue = "0x5ebacac108d665819398e5c37e12b0162d781398") String walletAddress) {
        TopUser topUserEntity = topUserService.getByWallet(walletAddress);
        return AjaxResult.success(topUserEntity);
    }

    /**
     * 用户注册
     *
     * @param loginBody 注册信息
     * @return 结果
     */
    @Operation(summary = "注册用户", description = "需要授权")
//    @TransactionVerifyCheck
    @PostMapping("/register")
    public AjaxResult register(@RequestBody WalletRegisterBody loginBody) {
        try {
            boolean validateResult = UnsignMessageUtils.validate(loginBody.getSignMsg(), loginBody.getContent(), loginBody.getWallet());
            if (!validateResult) {
                return AjaxResult.error("validate sign error!");
            }
        } catch (SignatureException e) {
            log.error("签名错误", e);
            throw new ServiceException("签名错误");
        }
        //check the user wallet is existed.
        Optional<TopUser> byWalletOptional = topUserService.getByWalletOptional(loginBody.getWallet());
        if (byWalletOptional.isPresent()) {
            return AjaxResult.error("wallet is exist");
        }

        //检查邀请码用户是否存在
        Optional<TopUser> inviteOpt = topUserService.getByInviteCode(loginBody.getInvitedCode());
        if (!inviteOpt.isPresent()) {
            return AjaxResult.error("invite code is not exist");
        }

        TopUser topUserEntity = new TopUser();
        BeanUtils.copyProperties(loginBody, topUserEntity);
        topUserEntity.setWallet(topUserEntity.getWallet().toLowerCase());
        topUserEntity.setGrade(0);
        topUserEntity.setCreateTime(LocalDateTime.now());
        topUserEntity.setUpdateTime(LocalDateTime.now());
        topUserEntity.setInvitedUserId(inviteOpt.get().getId());
        topUserEntity.setInvitedUserCode(loginBody.getInvitedCode());
        // 生成邀请码.
        topUserEntity.setInvitedCode(NumbersUtils.createInvite());
        topUserService.save(topUserEntity);
        // 绑定邀请数据
        inviteService.process(topUserEntity.getId(), topUserEntity.getInvitedUserId());
        return AjaxResult.success("Success");
    }

    @Operation(summary = "设置用户的BTC提现地址")
    @PostMapping("updateWithdrawBTCAddress")
    public AjaxResult<String> updateWithdrawBTCAddress(@RequestBody BTCAddressBody btcAddressBody) {
        try {
            boolean validateResult = UnsignMessageUtils.validate(btcAddressBody.getSignMsg(), btcAddressBody.getContent(), btcAddressBody.getWallet());
            if (!validateResult) {
                return AjaxResult.error("validate sign error!");
            }
        } catch (SignatureException e) {
            log.error("签名错误", e);
            throw new ServiceException("签名错误");
        }
        String btcTransferAddress = btcAddressBody.getBtcTransferAddress();

        TopUser topUserEntity = topUserService.getByWallet(btcAddressBody.getWallet());
        topUserEntity.setBtcTransferAddress(btcTransferAddress);
        topUserService.updateById(topUserEntity);
        return AjaxResult.success("Success");
    }

    @Operation(summary = "获取个人分享数据")
    @GetMapping("/getInviteInfo")
    public AjaxResult getInviteInfo() {
        return AjaxResult.success(topUserService.getInviteInfo(RequestUtil.getWallet()));
    }

    @Operation(summary = "我的分享")
    @GetMapping("/getInviteList")
    public AjaxResult getInviteList() {
        return AjaxResult.success(topUserService.getInviteList(RequestUtil.getWallet()));
    }

    @Autowired
    private TopTaskProcessService taskProcessService;

    @Autowired
    private MiningProcessService processService;

    @Autowired
    private TopStoreOrderService storeOrderService;

    @GetMapping("/processMining")
    public AjaxResult processMining(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        while (!start.isAfter(end)) {
            String processNo = TopNo.PROCESS_NO._code + IdUtil.getSnowflake(TopNo.PROCESS_NO._workId).nextIdStr();
            taskProcessService.start(processNo, start, 1);
            processService.process(start);
            taskProcessService.end(processNo);
            start = start.plusDays(1);
        }
        return AjaxResult.success();
    }

    @GetMapping("/processStore")
    public AjaxResult processStore(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        while (!start.isAfter(end)) {
            String processNo = TopNo.PROCESS_NO._code + IdUtil.getSnowflake(TopNo.PROCESS_NO._workId).nextIdStr();
            taskProcessService.start(processNo, start, 2);
            storeOrderService.process(start);
            taskProcessService.end(processNo);
            start = start.plusDays(1);
        }
        return AjaxResult.success();
    }

}
