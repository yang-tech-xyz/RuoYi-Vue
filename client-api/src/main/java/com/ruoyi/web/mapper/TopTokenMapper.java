package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.entity.TopToken;
import com.ruoyi.web.vo.TokenVO;
import com.ruoyi.web.vo.TopTokenChainVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 币种配置Mapper接口
 *
 * @author ruoyi
 * @date 2024-02-03
 */
@Repository
public interface TopTokenMapper extends BaseMapper<TopToken> {

    @Select("select tt.id,tt.symbol ,tt.online ,ttc.erc20_address,ttc.chain_id,tt.create_time ,tt.update_time ,tt.create_by ,tt.update_by,tc.receive_address " +
            "from top_token tt \n" +
            "left join top_token_chain ttc on tt.id = ttc.token_id \n" +
            "left join top_chain tc on tc.chain_id = ttc.chain_id \n" +
            "where tt.online = 1\n" +
            "and ttc.chain_id = #{chainId}\n")
    List<TopTokenChainVO> queryTokensByChainId(String chainId);


    @Select("select tt.id,tt.symbol ,tt.online ,ttc.erc20_address,ttc.chain_id,tt.create_time ,tt.update_time ,tt.create_by ,tt.update_by " +
            "from top_token_chain ttc \n" +
            "left join top_token tt on tt.id = ttc.token_id \n" +
            "where tt.online = 1\n" +
            "and ttc.chain_id = #{chainId}\n" +
            "and ttc.token_id = #{tokenId}\n"
    )
    Optional<TopTokenChainVO> queryTokenByTokenIdAndChainId(@Param("tokenId") Integer tokenId, @Param("chainId") Long chainId);

    List<TokenVO> selectOnlineListVO();

}
