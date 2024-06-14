package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopTokenBurning;
import com.ruoyi.web.mapper.TopTokenBurningMapper;
import com.ruoyi.web.vo.TokenBurningVO;
import org.springframework.stereotype.Service;

@Service
public class TopTokenBurningService extends ServiceImpl<TopTokenBurningMapper, TopTokenBurning> {

    public TokenBurningVO getBurning() {
        return baseMapper.selectBySymbol("BTCF");
    }
}
