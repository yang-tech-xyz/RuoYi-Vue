package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.dto.StoreOrderPageDTO;
import com.ruoyi.admin.entity.TopStoreOrder;
import com.ruoyi.admin.mapper.TopStoreOrderMapper;
import com.ruoyi.admin.vo.AccountTxVO;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.admin.vo.StoreOrderVO;
import org.springframework.stereotype.Service;

@Service
public class TopStoreOrderService extends ServiceImpl<TopStoreOrderMapper, TopStoreOrder> {

    public PageVO<StoreOrderVO> getPage(StoreOrderPageDTO dto) {
        IPage<StoreOrderVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectPageVO(iPage, dto);
        PageVO<StoreOrderVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }
}
