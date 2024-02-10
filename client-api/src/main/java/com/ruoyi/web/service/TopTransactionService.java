package com.ruoyi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopTransaction;
import com.ruoyi.web.enums.TransactionType;
import com.ruoyi.web.mapper.TopTransactionMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopTransactionService extends ServiceImpl<TopTransactionMapper, TopTransaction> {
    public List<TopTransaction> queryRechargeUnConfirm() {
        LambdaQueryWrapper<TopTransaction> query = Wrappers.lambdaQuery();
        query.eq(TopTransaction::getIsConfirm,0).eq(TopTransaction::getType, TransactionType.Recharge);
        return this.list(query);
    }

    public List<TopTransaction> queryWithdrawUnConfirm() {
        LambdaQueryWrapper<TopTransaction> query = Wrappers.lambdaQuery();
        query.eq(TopTransaction::getIsConfirm,0).eq(TopTransaction::getType, TransactionType.Withdraw);
        return this.list(query);
    }

    public Optional<TopTransaction> getTransactionByHash(String hash) {
        LambdaQueryWrapper<TopTransaction> query = Wrappers.lambdaQuery();
        query.eq(TopTransaction::getHash,hash);
        return this.getOneOpt(query);
    }

    /**
     * 事务确认
     * @param topTransaction
     */
    public void updateConfirm(TopTransaction topTransaction) {
        this.baseMapper.updateConfirm(topTransaction.getId());
    }
}
