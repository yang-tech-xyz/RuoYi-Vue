package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.vo.AdminVO;
import org.springframework.stereotype.Repository;
import com.ruoyi.admin.entity.TopAdmin;

import java.util.List;

@Repository
public interface TopAdminMapper extends BaseMapper<TopAdmin>{

    List<AdminVO> selectListVO();

}

