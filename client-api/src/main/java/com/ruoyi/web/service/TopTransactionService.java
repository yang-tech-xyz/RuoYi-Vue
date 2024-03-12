package com.ruoyi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.common.CommonStatus;
import com.ruoyi.web.dto.TopTransactionDTO;
import com.ruoyi.web.entity.TopTransaction;
import com.ruoyi.web.enums.TransactionType;
import com.ruoyi.web.mapper.TopTransactionMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopTransactionService extends ServiceImpl<TopTransactionMapper, TopTransaction> {
    public List<TopTransaction> queryRechargeUnConfirm() {
        LambdaQueryWrapper<TopTransaction> query = Wrappers.lambdaQuery();
        query.eq(TopTransaction::getIsConfirm, CommonStatus.UN_CONFIRM).eq(TopTransaction::getType, TransactionType.Recharge).ne(TopTransaction::getChainId,-1);
        return this.list(query);
    }
    public List<TopTransaction> queryTronRechargeUnConfirm() {
        LambdaQueryWrapper<TopTransaction> query = Wrappers.lambdaQuery();
        query.eq(TopTransaction::getIsConfirm, CommonStatus.UN_CONFIRM).eq(TopTransaction::getType, TransactionType.Recharge).eq(TopTransaction::getChainId,-1);
        return this.list(query);
    }

//    public List<TopTransaction> queryWithdrawUnConfirm() {
//        LambdaQueryWrapper<TopTransaction> query = Wrappers.lambdaQuery();
//        query.eq(TopTransaction::getIsConfirm,0).eq(TopTransaction::getType, TransactionType.Withdraw);
//        return this.list(query);
//    }

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

    /**
     * 事务确认
     * @param hash
     */
    public void updateFailed(String hash) {
        this.baseMapper.updateFailed(hash);
    }

    public IPage<TopTransaction> getTransaction(TopTransactionDTO topTransaction) {
        IPage<TopTransaction> page = new Page<>(topTransaction.getPageNum(), topTransaction.getPageSize());
        LambdaQueryWrapper<TopTransaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(topTransaction.getHash()),TopTransaction::getHash,topTransaction.getHash())
                .eq(StringUtils.isNotEmpty(topTransaction.getStatus()),TopTransaction::getStatus,topTransaction.getStatus())
                .orderByDesc(TopTransaction::getCreateTime);
        return this.getBaseMapper().selectPage(page,wrapper);
    }
}
