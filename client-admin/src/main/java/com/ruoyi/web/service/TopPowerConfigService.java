package com.ruoyi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopPowerConfig;
import com.ruoyi.web.entity.TopTransaction;
import com.ruoyi.web.mapper.TopPowerConfigMapper;
import com.ruoyi.web.mapper.TopTransactionMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopPowerConfigService extends ServiceImpl<TopPowerConfigMapper, TopPowerConfig> {
}
