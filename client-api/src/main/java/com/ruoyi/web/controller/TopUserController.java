package com.ruoyi.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.VO.WalletRegisterBody;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.service.TopUserService;
import com.ruoyi.web.utils.NumbersUtils;
import com.ruoyi.web.utils.UnsignMessageUtils;
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
public class TopUserController
{
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
    public AjaxResult register(@RequestBody WalletRegisterBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        try {
            boolean validateResult = UnsignMessageUtils.validate(loginBody.getSignMsg(),"青年人的责任重大！努力吧...",loginBody.getWalletAddress());
            if(!validateResult){
                return AjaxResult.error("validate sign error!");
            }
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        //check the user wallet is exist.
        LambdaQueryWrapper<TopUserEntity> queryWallet = Wrappers.lambdaQuery();
        queryWallet.eq(TopUserEntity::getWallet,loginBody.getWalletAddress());
        Optional userOpt = topUserService.getOneOpt(queryWallet);
        if(userOpt.isPresent()){
            return AjaxResult.error("wallet is exist");
        }

        //检查邀请码用户是否存在
        TopUserEntity queryInviteEntity = new TopUserEntity();
        queryInviteEntity.setInvitedCode(loginBody.getInvitedCode());

        LambdaQueryWrapper queryInvite = Wrappers.lambdaQuery(queryInviteEntity);
        Optional inviteOpt = topUserService.getOneOpt(queryInvite);
        if(!inviteOpt.isPresent()){
            return AjaxResult.error("invite code is not exist");
        }

        TopUserEntity topUserEntity = new TopUserEntity();
        BeanUtils.copyProperties(loginBody,topUserEntity);
        topUserEntity.setCreateTime(LocalDateTime.now());
        topUserEntity.setUpdateTime(LocalDateTime.now());
        // 生成邀请码.
        topUserEntity.setInvitedCode(NumbersUtils.createInvite());
        topUserService.save(topUserEntity);
        return ajax;
    }


}
