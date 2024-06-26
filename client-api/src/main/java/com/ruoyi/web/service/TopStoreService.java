package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopStore;
import com.ruoyi.web.mapper.TopStoreMapper;
import com.ruoyi.web.vo.StoreVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TopStoreService extends ServiceImpl<TopStoreMapper, TopStore> {

    public List<StoreVO> getList() {
        return baseMapper.selectValidList();
    }
}
