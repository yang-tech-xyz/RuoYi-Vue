package com.ruoyi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopTokenPrice;
import com.ruoyi.web.mapper.TopTokenMapper;
import com.ruoyi.web.mapper.TopTokenPriceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class TopTokenPriceService extends ServiceImpl<TopTokenPriceMapper, TopTokenPrice> {

    @Autowired
    private TopTokenMapper topTokenMapper;

    /*@Autowired
    private GateIOFeign gateIOFeign;*/

    /**
     * 查询币种价格
     */
    public BigDecimal getPrice(String token) {
        Optional<TopTokenPrice> optional = Optional.ofNullable(baseMapper.selectOne(new LambdaQueryWrapper<TopTokenPrice>()
                .eq(TopTokenPrice::getSymbol, token)));
        if (optional.isPresent()) {
            return optional.get().getPrice();
        }
        return BigDecimal.ZERO;

    }

    /**
     * 刷新币种价格
     */
    public void refPrice() {
       /* List<TopToken> tokens = topTokenMapper.selectList(new LambdaQueryWrapper<TopToken>());
        for (TopToken token : tokens) {
            try {
                Optional<TopTokenPrice> optional = Optional.ofNullable(baseMapper.selectOne(new LambdaQueryWrapper<TopTokenPrice>()
                        .eq(TopTokenPrice::getSymbol, token.getSymbol())));
                BigDecimal price = BigDecimal.ONE;
                if (!token.getSymbol().equals("USDT")) {
                    List<TickerVO> tickerVOS = gateIOFeign.getTickers(token.getSymbol() + "_" + "USDT");
                    price = tickerVOS.get(0).getLast();
                }
                if (optional.isPresent()) {
                    TopTokenPrice tokenPrice = optional.get();
                    tokenPrice.setPrice(price);
                    tokenPrice.setUpdatedBy("SYS");
                    tokenPrice.setUpdatedDate(LocalDateTime.now());
                    baseMapper.updateById(tokenPrice);
                } else {
                    TopTokenPrice tokenPrice = new TopTokenPrice();
                    tokenPrice.setSymbol(token.getSymbol());
                    tokenPrice.setPrice(price);
                    tokenPrice.setCreatedBy("SYS");
                    tokenPrice.setCreatedDate(LocalDateTime.now());
                    tokenPrice.setUpdatedBy("SYS");
                    tokenPrice.setUpdatedDate(LocalDateTime.now());
                    baseMapper.insert(tokenPrice);
                }
            } catch (Exception ex) {
                log.error("token price error:{}", token.getSymbol(), ex);
            }
        }*/
    }
}