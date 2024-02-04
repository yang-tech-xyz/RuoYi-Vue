package com.ruoyi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopToken;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.mapper.TopTokenMapper;
import com.ruoyi.web.mapper.TopUserMapper;
import com.ruoyi.web.vo.TopTokenChainVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopTokenService extends ServiceImpl<TopTokenMapper, TopToken> {
    public List<TopTokenChainVO> queryTokensByChainId(String chainId) {
        return this.baseMapper.queryTokensByChainId(chainId);
    }

    public Optional<TopToken> queryTokenBySymbol(String tokenSymbol) {
        LambdaQueryWrapper<TopToken> query = Wrappers.lambdaQuery();
        query.eq(TopToken::getSymbol,tokenSymbol);
        return this.getOneOpt(query);
    }

    public Optional<TopTokenChainVO> queryTokenByTokenIdAndChainId(Integer tokenId, Long chainId) {
        return this.baseMapper.queryTokenByTokenIdAndChainId(tokenId,chainId);
    }
}
