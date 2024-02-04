package com.ruoyi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopTransaction;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.mapper.TopTransactionMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopTransactionService extends ServiceImpl<TopTransactionMapper, TopTransaction> {
    public Optional<TopTransaction> getTransactionByHash(String hash) {
        LambdaQueryWrapper<TopTransaction> query = Wrappers.lambdaQuery();
        query.eq(TopTransaction::getHash,hash);
        return this.getOneOpt(query);
    }
}
