package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.vo.StoreVO;
import org.springframework.stereotype.Repository;
import com.ruoyi.web.entity.TopStore;

import java.util.List;

@Repository
public interface TopStoreMapper extends BaseMapper<TopStore>{

    List<StoreVO> selectValidList();
}

