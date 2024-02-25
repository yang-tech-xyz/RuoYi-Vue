package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.web.entity.TopUser;
import com.ruoyi.web.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopUserMapper extends BaseMapper<TopUser> {

    TopUser selectByWallet(@Param("wallet") String wallet);

    TopUser selectParent(@Param("parentId") Long parentId);

    TopUser lockById(@Param("id") Long id);

    List<UserProcessVO> selectUserVOList();

    PowerInviteInfoVO selectPowerInviteInfo(@Param("wallet") String wallet);

    IPage<PowerInviteVO> selectPowerPageVO(@Param("iPage") IPage<PowerInviteVO> iPage, @Param("userId") Long userId);

    StoreInviteInfoVO selectStoreInviteInfo(@Param("wallet") String wallet);

    IPage<StoreInviteVO> selectStorePageVO(@Param("iPage") IPage<StoreInviteVO> iPage, @Param("userId") Long userId);
}
