package com.ruoyi.admin.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.dto.UserInfoPageDTO;
import com.ruoyi.admin.entity.TopUserInfo;
import com.ruoyi.admin.vo.UserInfoVO;

@Repository
public interface TopUserInfoMapper extends BaseMapper<TopUserInfo> {

    IPage<UserInfoVO> selectPageVO(@Param("iPage") IPage<UserInfoVO> iPage, @Param("dto") UserInfoPageDTO dto);
}
