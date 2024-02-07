package com.ruoyi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopChain;
import com.ruoyi.web.entity.TopToken;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.mapper.TopChainMapper;
import com.ruoyi.web.mapper.TopTokenMapper;
import com.ruoyi.web.vo.TopTokenChainVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopChainService extends ServiceImpl<TopChainMapper, TopChain> {
    public Optional<TopChain> getOptByChainId(Long chainId) {
        LambdaQueryWrapper<TopChain> query = Wrappers.lambdaQuery();
        query.eq(TopChain::getChainId,chainId);
        return this.getOneOpt(query);
    }
}
