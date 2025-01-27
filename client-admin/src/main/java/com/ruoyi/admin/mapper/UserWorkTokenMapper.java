package com.ruoyi.admin.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.dto.UserWorkTokenDTO;
import com.ruoyi.admin.entity.UserWorkToken;
import com.ruoyi.admin.vo.UserWorkTokenVO;

@Repository
public interface UserWorkTokenMapper extends BaseMapper<UserWorkToken> {

    @Select("SELECT wu.id,wu.uid,wu.wallet_address ,ui.create_time  FROM web3_user wu \n" +
            "             left join user_info ui on ui.uid = wu.uid")
    IPage<UserWorkTokenVO> selectPageVO(@Param("iPage") IPage<UserWorkTokenVO> iPage, @Param("dto") UserWorkTokenDTO dto);
}
