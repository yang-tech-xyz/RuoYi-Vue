package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.entity.TopUser;
import com.ruoyi.admin.vo.InviteInfoVO;
import com.ruoyi.admin.vo.InviteVO;
import com.ruoyi.admin.vo.UserProcessVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopUserMapper extends BaseMapper<TopUser> {

    TopUser selectByWalletAddress(@Param("walletAddress") String walletAddress);

    List<UserProcessVO> selectUserVOList();

    List<InviteVO> selectInviteListById(@Param("id") Long id);

    InviteInfoVO selectInviteInfo(@Param("walletAddress") String walletAddress);

    TopUser selectParent(@Param("parentId") Long parentId);
}
