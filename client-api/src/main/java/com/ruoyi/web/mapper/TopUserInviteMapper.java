package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.ruoyi.web.entity.TopUserInvite;

@Repository
public interface TopUserInviteMapper extends BaseMapper<TopUserInvite>{

    TopUserInvite selectInvite(@Param("userId") Long userId, @Param("inviteUserId") Long inviteUserId);
}

