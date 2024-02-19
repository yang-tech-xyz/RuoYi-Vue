package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.entity.TopToken;
import com.ruoyi.admin.vo.TokenVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 币种配置Mapper接口
 *
 * @author ruoyi
 * @date 2024-02-03
 */
@Repository
public interface TopTokenMapper extends BaseMapper<TopToken> {

    List<TokenVO> selectListVO();

}
