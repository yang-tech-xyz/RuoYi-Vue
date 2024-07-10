package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.dto.TokenBurningDTO;
import com.ruoyi.admin.entity.TopTokenBurning;
import com.ruoyi.admin.mapper.TopTokenBurningMapper;
import com.ruoyi.admin.vo.TokenBurningVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class TopTokenBurningService extends ServiceImpl<TopTokenBurningMapper, TopTokenBurning> {

    public TokenBurningVO getBurning() {
        return baseMapper.selectBySymbol("BTCF");
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean burning(TokenBurningDTO dto) {
        TopTokenBurning burning = baseMapper.lockBySymbol(dto.getSymbol());
        burning.setBurningAmount(burning.getBurningAmount().add(dto.getBurningAmount()));
        burning.setUpdatedDate(LocalDateTime.now());
        burning.setUpdatedBy("SYS");
        baseMapper.updateById(burning);
        return true;
    }
}
