package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.entity.TopStore;
import com.ruoyi.admin.vo.StoreVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopStoreMapper extends BaseMapper<TopStore>{

    List<StoreVO> selectValidList();
}

