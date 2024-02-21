package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.entity.TopTokenChain;
import com.ruoyi.admin.vo.TopTokenChainVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 币种配置Mapper接口
 *
 * @author ruoyi
 * @date 2024-02-03
 */
@Repository
public interface TopTokenChainMapper extends BaseMapper<TopTokenChain> {


    @Select("SELECT ttc.id,ttc.*,tt.symbol ,tc.chain_name as chainName FROM top_token_chain ttc \n" +
            "LEFT JOIN top_token tt ON tt.id =ttc.token_id \n" +
            "LEFT JOIN top_chain tc on tc.id = ttc.chain_id ")
    List<TopTokenChainVO> selectListVO();

}
