package com.ruoyi.web.controller;

import cn.hutool.core.util.IdUtil;
import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.enums.TopNo;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.service.MiningProcessService;
import com.ruoyi.web.service.TopTaskProcessService;
import com.ruoyi.web.service.TopUserService;
import com.ruoyi.web.utils.NumbersUtils;
import com.ruoyi.web.utils.RequestUtil;
import com.ruoyi.web.utils.UnsignMessageUtils;
import com.ruoyi.web.vo.WalletRegisterBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/topUser")
@Tag(description = "TopUserController", name = "用户信息")
@RestController
public class TopUserController {
    @Autowired
    private TopUserService topUserService;

    @Operation(summary = "用户查询", description = "用户查询")
    @GetMapping("")
    public AjaxResult<TopUserEntity> queryUser(@RequestHeader(value = "WalletAddress", defaultValue = "0x5ebacac108d665819398e5c37e12b0162d781398") String walletAddress) {
        Optional<TopUserEntity> topUserEntityOptional = topUserService.getByWallet(walletAddress);
        if (!topUserEntityOptional.isPresent()) {
            throw new ServiceException("user not exist!");
        }
        TopUserEntity topUserEntity = topUserEntityOptional.get();
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
        AjaxResult ajax = AjaxResult.success();
        try {
            boolean validateResult = UnsignMessageUtils.validate(loginBody.getSignMsg(), loginBody.getContent(), loginBody.getWallet());
            if (!validateResult) {
                return AjaxResult.error("validate sign error!");
            }
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        //check the user wallet is existed.
        Optional<TopUserEntity> userOpt = topUserService.getByWallet(loginBody.getWallet());
        if (userOpt.isPresent()) {
            return AjaxResult.error("wallet is exist");
        }

        //检查邀请码用户是否存在
        Optional<TopUserEntity> inviteOpt = topUserService.getByInviteCode(loginBody.getInvitedCode());
        if (!inviteOpt.isPresent()) {
            return AjaxResult.error("invite code is not exist");
        }

        TopUserEntity topUserEntity = new TopUserEntity();
        BeanUtils.copyProperties(loginBody, topUserEntity);
        topUserEntity.setWallet(topUserEntity.getWallet().toLowerCase());
        topUserEntity.setCreateTime(LocalDateTime.now());
        topUserEntity.setUpdateTime(LocalDateTime.now());
        topUserEntity.setInvitedUserId(inviteOpt.get().getId());
        topUserEntity.setInvitedUserCode(loginBody.getInvitedCode());
        // 生成邀请码.
        topUserEntity.setInvitedCode(NumbersUtils.createInvite());
        topUserService.save(topUserEntity);
        return ajax;
    }

    @Operation(summary = "获取个人分享数据")
    @GetMapping("/getInviteInfo")
    public AjaxResult getInviteInfo() {
        return AjaxResult.success(topUserService.getInviteInfo(RequestUtil.getWalletAddress()));
    }

    @Operation(summary = "我的分享")
    @GetMapping("/getInviteList")
    public AjaxResult getInviteList() {
        return AjaxResult.success(topUserService.getInviteList(RequestUtil.getWalletAddress()));
    }

    @Autowired
    private TopTaskProcessService taskProcessService;

    @Autowired
    private MiningProcessService processService;

    @GetMapping("/process")
    public AjaxResult process(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                              @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        while (!start.isAfter(end)) {
            String processNo = TopNo.PROCESS_NO._code + IdUtil.getSnowflake(TopNo.PROCESS_NO._workId).nextIdStr();
            taskProcessService.start(processNo, start);
            processService.process(start);
            taskProcessService.end(processNo);
            start = start.plusDays(1);
        }
        return AjaxResult.success();
    }

}
