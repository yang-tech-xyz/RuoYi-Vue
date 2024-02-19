package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.dto.TokenAddDTO;
import com.ruoyi.admin.dto.TokenUpdateDTO;
import com.ruoyi.admin.entity.TopToken;
import com.ruoyi.admin.mapper.TopTokenMapper;
import com.ruoyi.admin.vo.TokenVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TopTokenService extends ServiceImpl<TopTokenMapper, TopToken> {

    public List<TokenVO> getList() {
        return baseMapper.selectListVO();
    }

    public Boolean add(TokenAddDTO dto) {
        TopToken token = new TopToken();
        BeanUtils.copyProperties(dto, token);
        token.setCreateBy("SYS");
        token.setCreateTime(LocalDateTime.now());
        token.setUpdateBy("SYS");
        token.setUpdateTime(LocalDateTime.now());
        baseMapper.insert(token);
        return true;
    }

    public Boolean edit(TokenUpdateDTO dto) {
        TopToken token = baseMapper.selectOne(new LambdaQueryWrapper<TopToken>().eq(TopToken::getSymbol, dto.getSymbol()));
        BeanUtils.copyProperties(dto, token);
        token.setUpdateBy("SYS");
        token.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(token);
        return true;
    }
}
