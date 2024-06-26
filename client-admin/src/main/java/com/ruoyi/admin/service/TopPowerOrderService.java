package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.dto.PowerOrderPageDTO;
import com.ruoyi.admin.entity.TopPowerOrder;
import com.ruoyi.admin.mapper.TopPowerOrderMapper;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.admin.vo.PowerOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TopPowerOrderService extends ServiceImpl<TopPowerOrderMapper, TopPowerOrder> {

    public PageVO<PowerOrderVO> getPage(PowerOrderPageDTO dto) {
        IPage<PowerOrderVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectPageVO(iPage, dto);
        PageVO<PowerOrderVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }
}
