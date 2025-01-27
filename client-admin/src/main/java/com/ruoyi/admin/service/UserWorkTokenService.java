package com.ruoyi.admin.service;

import cn.hutool.core.util.RandomUtil;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.dto.UserWorkTokenDTO;
import com.ruoyi.admin.entity.UserWorkToken;
import com.ruoyi.admin.mapper.UserWorkTokenMapper;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.admin.vo.UserWorkTokenVO;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class UserWorkTokenService extends ServiceImpl<UserWorkTokenMapper, UserWorkToken> {

    public PageVO<UserWorkTokenVO> getPage(UserWorkTokenDTO dto) {
        IPage<UserWorkTokenVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectPageVO(iPage, dto);
        PageVO<UserWorkTokenVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());

        List<UserWorkTokenVO> records = iPage.getRecords();
        List result = records.stream().map((t)->{
            t.setInvitedAmount(RandomUtil.randomInt(0, 20));
            t.setTokenBalance(new BigDecimal(RandomUtil.randomInt(1, 12)*5));
            return t;
        }).toList();

        pageVO.setList(result);
        return pageVO;
    }
}
