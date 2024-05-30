package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.dto.PowerOrderIncomePageDTO;
import com.ruoyi.admin.entity.TopPowerOrderIncome;
import com.ruoyi.admin.mapper.TopPowerOrderIncomeMapper;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.admin.vo.PowerOrderIncomeStatisticsVO;
import com.ruoyi.admin.vo.PowerOrderIncomeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TopPowerOrderIncomeService extends ServiceImpl<TopPowerOrderIncomeMapper, TopPowerOrderIncome> {

    public PowerOrderIncomeStatisticsVO getStatistics(PowerOrderIncomePageDTO dto) {
        return baseMapper.selectStatistics(dto);
    }

    public PageVO<PowerOrderIncomeVO> getPage(PowerOrderIncomePageDTO dto) {
        IPage<PowerOrderIncomeVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectPageVO(iPage, dto);
        PageVO<PowerOrderIncomeVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }
}
