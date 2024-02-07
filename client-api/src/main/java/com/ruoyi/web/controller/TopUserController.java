package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.service.TopUserService;
import com.ruoyi.web.utils.NumbersUtils;
import com.ruoyi.web.utils.UnsignMessageUtils;
import com.ruoyi.web.vo.WalletRegisterBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SignatureException;
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


}
