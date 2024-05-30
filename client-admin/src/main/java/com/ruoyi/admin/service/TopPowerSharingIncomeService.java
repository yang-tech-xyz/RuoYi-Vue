package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.dto.PowerOrderSharingIncomePageDTO;
import com.ruoyi.admin.entity.TopPowerSharingIncome;
import com.ruoyi.admin.mapper.TopPowerSharingIncomeMapper;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.admin.vo.PowerOrderSharingIncomeStatisticsVO;
import com.ruoyi.admin.vo.PowerOrderSharingIncomeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TopPowerSharingIncomeService extends ServiceImpl<TopPowerSharingIncomeMapper, TopPowerSharingIncome> {

    public PowerOrderSharingIncomeStatisticsVO getStatistics(PowerOrderSharingIncomePageDTO dto) {
        return baseMapper.selectStatistics(dto);
    }

    public PageVO<PowerOrderSharingIncomeVO> getPage(PowerOrderSharingIncomePageDTO dto) {
        IPage<PowerOrderSharingIncomeVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectPageVO(iPage, dto);
        PageVO<PowerOrderSharingIncomeVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }
}
