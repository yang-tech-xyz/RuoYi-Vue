package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.dto.TopTokenChainDTO;
import com.ruoyi.admin.entity.TopTokenChain;
import com.ruoyi.admin.exception.ServiceException;
import com.ruoyi.admin.mapper.TopTokenChainMapper;
import com.ruoyi.admin.vo.TopTokenChainVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TopTokenChainService extends ServiceImpl<TopTokenChainMapper, TopTokenChain> {

    public List<TopTokenChainVO> getList() {
        return baseMapper.selectListVO();
    }

    public Boolean add(TopTokenChainDTO topTokenChainDTO) {
        TopTokenChain topTokenChain = new TopTokenChain();
        BeanUtils.copyProperties(topTokenChainDTO, topTokenChain);
        topTokenChain.setCreateBy("SYS");
        topTokenChain.setCreateTime(LocalDateTime.now());
        topTokenChain.setUpdateBy("SYS");
        topTokenChain.setUpdateTime(LocalDateTime.now());
        baseMapper.insert(topTokenChain);
        return true;
    }

    public Boolean edit(TopTokenChainDTO topTokenChainDTO) {
        TopTokenChain token = baseMapper.selectOne(new LambdaQueryWrapper<TopTokenChain>()
                .eq(TopTokenChain::getChainId, topTokenChainDTO.getChainId())
                .eq(TopTokenChain::getTokenId,topTokenChainDTO.getTokenId())
        );
        if(token==null){
            throw new ServiceException("token chain not exist!");
        }
        BeanUtils.copyProperties(topTokenChainDTO, token);
        token.setUpdateBy("SYS");
        token.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(token);
        return true;
    }

}
