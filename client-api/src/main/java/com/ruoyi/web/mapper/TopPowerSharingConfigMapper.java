package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.vo.PowerSharingConfigVO;
import org.springframework.stereotype.Repository;
import com.ruoyi.web.entity.TopPowerSharingConfig;

import java.util.List;

@Repository
public interface TopPowerSharingConfigMapper extends BaseMapper<TopPowerSharingConfig>{

    List<PowerSharingConfigVO> selectListVO();

}

