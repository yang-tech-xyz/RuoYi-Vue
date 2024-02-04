package com.ruoyi.web.feign;

import com.ruoyi.web.feign.back.GateIOFeignFallBack;
import com.ruoyi.web.vo.TickerVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "GateIO", url = "https://api.gateio.ws", fallbackFactory = GateIOFeignFallBack.class)
public interface GateIOFeign {

    /**
     * https://api.gateio.ws/api/v4/spot/tickers?currency_pair=GT_USDT
     */
    @GetMapping("api/v4/spot/tickers")
    public List<TickerVO> getTickers(@RequestParam(value = "currency_pair") String currency_pair);

}
