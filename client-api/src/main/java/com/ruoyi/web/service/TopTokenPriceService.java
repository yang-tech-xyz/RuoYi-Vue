package com.ruoyi.web.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopToken;
import com.ruoyi.web.entity.TopTokenPrice;
import com.ruoyi.web.enums.Plate;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.mapper.TopTokenMapper;
import com.ruoyi.web.mapper.TopTokenPriceMapper;
import com.ruoyi.web.vo.TickerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TopTokenPriceService extends ServiceImpl<TopTokenPriceMapper, TopTokenPrice> {

    @Autowired
    private TopTokenMapper topTokenMapper;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 查询币种价格
     */
    public BigDecimal getPrice(String token) {
        Optional<TopTokenPrice> optional = Optional.ofNullable(baseMapper.selectOne(new LambdaQueryWrapper<TopTokenPrice>()
                .eq(TopTokenPrice::getSymbol, token)));
        if (optional.isPresent()) {
            BigDecimal price = optional.get().getPrice();
            if (price.compareTo(BigDecimal.ZERO) == 0) {
                throw new ServiceException("无法获取价格", 500);
            }
            return price;
        }
        throw new ServiceException("无法获取价格", 500);
    }

    /**
     * 刷新币种价格
     * 1.对接不同平台价格
     */
    public void refPrice() {
        List<TopToken> tokens = topTokenMapper.selectList(new LambdaQueryWrapper<TopToken>());
        for (TopToken token : tokens) {
            try {
                Optional<TopTokenPrice> optional = Optional.ofNullable(baseMapper.selectOne(new LambdaQueryWrapper<TopTokenPrice>()
                        .eq(TopTokenPrice::getSymbol, token.getSymbol())));
                BigDecimal price = BigDecimal.ONE;
                if (!token.getSymbol().equals("USDT")) {
                    try {
                        if (Plate.GATE_IO._code.equals(token.getPlate())) {
                            String result = restTemplate.getForObject(Plate.GATE_IO._url + "/api/v4/spot/tickers?currency_pair=" + token.getSymbol() + "_" + "USDT", String.class);
                            List<TickerVO> tickerVOS = JSONArray.parseArray(result, TickerVO.class);
                            price = tickerVOS.get(0).getLast();
                        } else if (Plate.BINANCE._code.equals(token.getPlate())) {
                            String result = restTemplate.getForObject(Plate.BINANCE._url + "/api/v3/ticker/price?symbol=" + token.getSymbol() + "USDT", String.class);
                            JSONObject jsonObject = JSONObject.parseObject(result);
                            price = jsonObject.getBigDecimal("price");
                        } else if (Plate.OKX._code.equals(token.getPlate())) {
                            String result = restTemplate.getForObject(Plate.OKX._url + "/api/v5/market/ticker?instId=" + token.getSymbol() + "_" + "USDT-SWAP", String.class);
                            JSONObject jsonObject = JSONObject.parseObject(result);
                            price = jsonObject.getJSONArray("data").getJSONObject(0).getBigDecimal("last");
                        } else if (Plate.HUO_BI._code.equals(token.getPlate())) {
                            String result = restTemplate.getForObject(Plate.HUO_BI._url + "/market/detail/merged?symbol=" + token.getSymbol().toLowerCase() + "usdt", String.class);
                            JSONObject jsonObject = JSONObject.parseObject(result);
                            price = jsonObject.getJSONObject("tick").getBigDecimal("close");
                        }
                    } catch (Exception ignored) {
                    }
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
        }
    }
}