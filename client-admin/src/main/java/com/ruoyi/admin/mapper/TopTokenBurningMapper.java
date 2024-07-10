package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.vo.TokenBurningVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.ruoyi.admin.entity.TopTokenBurning;

@Repository
public interface TopTokenBurningMapper extends BaseMapper<TopTokenBurning>{

    TokenBurningVO selectBySymbol(@Param("symbol") String symbol);

    TopTokenBurning lockBySymbol(@Param("symbol") String symbol);

}

