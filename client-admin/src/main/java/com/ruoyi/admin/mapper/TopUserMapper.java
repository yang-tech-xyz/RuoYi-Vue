package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.dto.UserPageDTO;
import com.ruoyi.admin.entity.TopUser;
import com.ruoyi.admin.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopUserMapper extends BaseMapper<TopUser> {

    IPage<UserVO> selectPageVO(@Param("iPage") IPage<UserVO> iPage, @Param("dto") UserPageDTO dto);
}
