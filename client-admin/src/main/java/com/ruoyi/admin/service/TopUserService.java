package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.dto.UserPageDTO;
import com.ruoyi.admin.entity.TopUser;
import com.ruoyi.admin.mapper.TopUserMapper;
import com.ruoyi.admin.vo.AccountTxVO;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.admin.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TopUserService extends ServiceImpl<TopUserMapper, TopUser> {

    public PageVO<UserVO> getPage(UserPageDTO dto) {
        IPage<UserVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectPageVO(iPage, dto);
        PageVO<UserVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }
}
