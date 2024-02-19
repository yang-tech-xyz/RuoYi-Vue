package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.dto.StoreAddDTO;
import com.ruoyi.admin.dto.StoreUpdateDTO;
import com.ruoyi.admin.entity.TopStore;
import com.ruoyi.admin.mapper.TopStoreMapper;
import com.ruoyi.admin.vo.StoreVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TopStoreService extends ServiceImpl<TopStoreMapper, TopStore> {

    public List<StoreVO> getList() {
        return baseMapper.selectListVO();
    }

    public Boolean add(StoreAddDTO dto) {
        TopStore store = new TopStore();
        BeanUtils.copyProperties(dto, store);
        store.setCreatedBy("SYS");
        store.setCreatedDate(LocalDateTime.now());
        store.setUpdatedBy("SYS");
        store.setUpdatedDate(LocalDateTime.now());
        baseMapper.insert(store);
        return true;
    }

    public Boolean edit(StoreUpdateDTO dto) {
        TopStore store = baseMapper.selectOne(new LambdaQueryWrapper<TopStore>().eq(TopStore::getPeriod, dto.getPeriod()));
        BeanUtils.copyProperties(dto, store);
        store.setUpdatedBy("SYS");
        store.setUpdatedDate(LocalDateTime.now());
        baseMapper.updateById(store);
        return true;
    }
}
