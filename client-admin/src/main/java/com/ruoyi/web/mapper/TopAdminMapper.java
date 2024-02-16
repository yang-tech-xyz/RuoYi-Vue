package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.vo.AdminVO;
import org.springframework.stereotype.Repository;
import com.ruoyi.web.entity.TopAdmin;

import java.util.List;

@Repository
public interface TopAdminMapper extends BaseMapper<TopAdmin>{

    List<AdminVO> selectListVO();

}

