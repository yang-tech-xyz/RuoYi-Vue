package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.entity.TopUserInvite;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopUserInviteMapper extends BaseMapper<TopUserInvite>{

    TopUserInvite selectInvite(@Param("userId") Long userId, @Param("inviteUserId") Long inviteUserId);
}

