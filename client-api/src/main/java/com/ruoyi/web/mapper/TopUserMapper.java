package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.entity.TopUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TopUserMapper extends BaseMapper<TopUserEntity> {

    TopUserEntity selectByWalletAddress(@Param("walletAddress") String walletAddress);

}
