/*
package com.ruoyi.web.feign.back;

import com.ruoyi.web.feign.GateIOFeign;
import com.ruoyi.web.vo.TickerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GateIOFeignFallBack implements FallbackFactory<GateIOFeign> {
    @Override
    public GateIOFeign create(Throwable cause) {
        return new GateIOFeign() {
            @Override
            public List<TickerVO> getTickers(String currency_pair) {
                return null;
            }
        };
    }
}
*/
