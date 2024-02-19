package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.vo.PowerSharingConfigVO;
import org.springframework.stereotype.Repository;
import com.ruoyi.admin.entity.TopPowerSharingConfig;

import java.util.List;

@Repository
public interface TopPowerSharingConfigMapper extends BaseMapper<TopPowerSharingConfig>{

    List<PowerSharingConfigVO> selectListVO();

}

