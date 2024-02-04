package com.ruoyi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopTransaction;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.mapper.TopTransactionMapper;
import com.ruoyi.web.mapper.TopUserMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopTransactionService extends ServiceImpl<TopTransactionMapper, TopTransaction> {
}
