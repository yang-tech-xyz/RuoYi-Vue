package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.dto.AccountPageDTO;
import com.ruoyi.admin.entity.TopAccount;
import com.ruoyi.admin.mapper.TopAccountMapper;
import com.ruoyi.admin.vo.AccountVO;
import com.ruoyi.admin.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TopAccountService extends ServiceImpl<TopAccountMapper, TopAccount> {

    public PageVO<AccountVO> getPage(AccountPageDTO dto) {
        IPage<AccountVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectPageVO(iPage, dto);
        PageVO<AccountVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }
}
