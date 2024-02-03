package com.ruoyi.web.controller;

import com.ruoyi.common.AjaxResult;
import com.ruoyi.web.entity.TopToken;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.service.TopTokenService;
import com.ruoyi.web.service.TopUserService;
import com.ruoyi.web.utils.NumbersUtils;
import com.ruoyi.web.utils.UnsignMessageUtils;
import com.ruoyi.web.vo.TopTokenChainVO;
import com.ruoyi.web.vo.WalletRegisterBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 充值
 * 
 * @author ruoyi
 */
@RequestMapping("/token")
@Tag(description = "TokenController", name = "token信息")
@RestController
public class CoinController
{
    @Autowired
    private TopUserService topUserService;

    @Autowired
    private TopTokenService topTokenService;

    @Operation(summary = "根据链id查询所有支持的token")
    @GetMapping("queryTokensByChainId")
    public AjaxResult queryTokensByChainId(@Parameter String chainId){
        List<TopTokenChainVO> list= topTokenService.queryTokensByChainId(chainId);
        return AjaxResult.success("success",list);
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
    public AjaxResult register(@RequestBody WalletRegisterBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        try {
            boolean validateResult = UnsignMessageUtils.validate(loginBody.getSignMsg(),"青年人的责任重大！努力吧...",loginBody.getWallet());
            if(!validateResult){
                return AjaxResult.error("validate sign error!");
            }
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        //check the user wallet is existed.
        Optional<TopUserEntity> userOpt = topUserService.getByWallet(loginBody.getWallet());
        if(userOpt.isPresent()){
            return AjaxResult.error("wallet is exist");
        }

        //检查邀请码用户是否存在
        Optional<TopUserEntity> inviteOpt = topUserService.getByInviteCode(loginBody.getInvitedCode());
        if(!inviteOpt.isPresent()){
            return AjaxResult.error("invite code is not exist");
        }

        TopUserEntity topUserEntity = new TopUserEntity();
        BeanUtils.copyProperties(loginBody,topUserEntity);
        topUserEntity.setCreateTime(LocalDateTime.now());
        topUserEntity.setUpdateTime(LocalDateTime.now());
        topUserEntity.setInvitedUserCode(loginBody.getInvitedCode());
        // 生成邀请码.
        topUserEntity.setInvitedCode(NumbersUtils.createInvite());
        topUserService.save(topUserEntity);
        return ajax;
    }


}
