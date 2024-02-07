package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.vo.TokenPriceVO;
import org.springframework.stereotype.Repository;
import com.ruoyi.web.entity.TopTokenPrice;

import java.util.List;

@Repository
public interface TopTokenPriceMapper extends BaseMapper<TopTokenPrice>{

    List<TokenPriceVO> selectListVO();

}

