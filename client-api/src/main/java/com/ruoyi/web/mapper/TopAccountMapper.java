package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.ruoyi.web.entity.TopAccount;

@Repository
public interface TopAccountMapper extends BaseMapper<TopAccount>{

    TopAccount lockById(@Param("id") Long id);
}

