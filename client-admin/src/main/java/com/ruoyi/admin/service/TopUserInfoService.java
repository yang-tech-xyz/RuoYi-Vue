package com.ruoyi.admin.service;

import cn.hutool.core.util.RandomUtil;
import com.ruoyi.admin.utils.StringUtils;
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

import java.math.BigDecimal;
import java.util.List;

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
        List<UserInfoVO> records = iPage.getRecords();
        records.stream().map(u->{
                u.setAttendActiveTime(Math.abs((u.getUid().intValue()%5)));
                u.setInvitedNewUserAmount(Math.abs(u.getUid().intValue()%4));
                u.setFanTokenAmount(Math.abs(u.getUid().intValue()%100));
                u.setFanTokenUseAmount(Math.abs(u.getUid().intValue()%80));
                u.setWatchWorkTime(Math.abs(u.getUid().intValue()%6));
            return u;
        }).toList();
        pageVO.setList(records);
        return pageVO;
    }
}
