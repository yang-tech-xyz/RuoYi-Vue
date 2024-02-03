package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.entity.TopToken;
import com.ruoyi.web.vo.TopTokenChainVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 币种配置Mapper接口
 * 
 * @author ruoyi
 * @date 2024-02-03
 */
public interface TopTokenMapper extends BaseMapper<TopToken>
{

    @Select("select tt.id,tt.symbol ,tt.`decimal`,tt.online ,ttc.erc20_address,ttc.chain_id,tt.create_time ,tt.update_time ,tt.create_by ,tt.update_by " +
            "from top_token tt \n" +
            "left join top_token_chain ttc on tt.id = ttc.token_id \n" +
            "where tt.online = 0\n" +
            "and ttc.chain_id = #{chainId}\n")
    List<TopTokenChainVO> queryTokensByChainId(String chainId);
}
