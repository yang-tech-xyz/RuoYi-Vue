package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TopUserMapper extends BaseMapper<TopUserEntity> {

    TopUserEntity selectByWalletAddress(@Param("walletAddress") String walletAddress);

    List<UserVO> selectProcessVO();

}
