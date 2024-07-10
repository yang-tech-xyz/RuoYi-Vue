package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.entity.TopTokenBurning;
import com.ruoyi.web.vo.TokenBurningVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopTokenBurningMapper extends BaseMapper<TopTokenBurning> {

    TokenBurningVO selectBySymbol(@Param("symbol") String symbol);
}

