package com.ruoyi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopTokenPrice;
import com.ruoyi.web.feign.GateIOFeign;
import com.ruoyi.web.mapper.TopTokenPriceMapper;
import com.ruoyi.web.vo.TickerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TopTokenPriceService extends ServiceImpl<TopTokenPriceMapper, TopTokenPrice> {

    @Autowired
    private GateIOFeign gateIOFeign;

    /**
     * 查询币种价格
     */
    public BigDecimal getPrice(String token) {
        Optional<TopTokenPrice> optional = Optional.ofNullable(baseMapper.selectOne(new LambdaQueryWrapper<TopTokenPrice>()
                .eq(TopTokenPrice::getToken, token)));
        if (optional.isPresent()) {
            return optional.get().getPrice();
        }
        return BigDecimal.ZERO;

    }

    /**
     * 刷新币种价格
     */
    public void refPrice() {
        List<TopTokenPrice> tokenPriceList = baseMapper.selectList(new LambdaQueryWrapper<>());
        for (TopTokenPrice tokenPrice : tokenPriceList) {
            try {
                List<TickerVO> tickerVOS = gateIOFeign.getTickers(tokenPrice.getToken() + "_" + "USDT");
                tokenPrice.setPrice(tickerVOS.get(0).getLast());
                tokenPrice.setUpdatedDate(LocalDateTime.now());
                baseMapper.updateById(tokenPrice);
            } catch (Exception ex) {
                log.error("token price error:{}", tokenPrice.getToken(), ex);
            }
        }
    }
}