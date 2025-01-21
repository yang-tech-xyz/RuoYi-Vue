package com.ruoyi.admin.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.dto.UserInfoPageDTO;
import com.ruoyi.admin.entity.TopUserInfo;
import com.ruoyi.admin.mapper.TopUserInfoMapper;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.admin.vo.UserInfoVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TopUserInfoService extends ServiceImpl<TopUserInfoMapper, TopUserInfo> {

    public PageVO<UserInfoVO> getPage(UserInfoPageDTO dto) {
        IPage<UserInfoVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectPageVO(iPage, dto);
        PageVO<UserInfoVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }
}
