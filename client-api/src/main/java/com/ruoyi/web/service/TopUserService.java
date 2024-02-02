package com.ruoyi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.mapper.TopUserMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopUserService extends ServiceImpl<TopUserMapper, TopUserEntity> {
    public Optional<TopUserEntity> getByWallet(String walletAddress) {
        LambdaQueryWrapper<TopUserEntity> queryWallet = Wrappers.lambdaQuery();
        queryWallet.eq(TopUserEntity::getWallet,walletAddress);
        return this.getOneOpt(queryWallet);
    }

    public Optional<TopUserEntity> getByInviteCode(String invitedCode) {
        LambdaQueryWrapper<TopUserEntity> query = Wrappers.lambdaQuery();
        query.eq(TopUserEntity::getInvitedCode,invitedCode);
        return this.getOneOpt(query);
    }
}
