package com.ruoyi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopNotice;
import com.ruoyi.web.mapper.TopNoticeMapper;
import com.ruoyi.web.utils.RequestUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopNoticeService extends ServiceImpl<TopNoticeMapper, TopNotice> {

    public List<TopNotice> getList() {
        return baseMapper.selectList(new LambdaQueryWrapper<TopNotice>()
                .eq(TopNotice::getLang, RequestUtil.getLang())
                .orderByDesc(TopNotice::getSeq));
    }
}
