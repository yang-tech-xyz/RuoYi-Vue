package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.dto.TopTransactionDTO;
import com.ruoyi.admin.entity.TopTransaction;
import com.ruoyi.admin.enums.TransactionType;
import com.ruoyi.admin.exception.ServiceException;
import com.ruoyi.admin.mapper.TopTransactionMapper;
import com.ruoyi.admin.common.CommonStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class TopTransactionService extends ServiceImpl<TopTransactionMapper, TopTransaction> {

    public TopTransaction getOptByTransactionNo(String transactionNo) {
        LambdaQueryWrapper<TopTransaction> query = Wrappers.lambdaQuery();
        query.eq(TopTransaction::getTransNo,transactionNo);
        Optional<TopTransaction> topTransaction = this.getOneOpt(query);
        if(topTransaction.isEmpty()){
            log.warn("transaction is not exist,transactionNo is:{}",transactionNo);
            throw new ServiceException("transaction is not exist");
        }
        return topTransaction.get();
    }

//    public void updateConfirm(TopTransaction topTransaction) {
//        this.baseMapper.updateConfirm(topTransaction.getId());
//    }

    public Optional<TopTransaction> getTransactionByHash(String hash) {
        LambdaQueryWrapper<TopTransaction> query = Wrappers.lambdaQuery();
        query.eq(TopTransaction::getHash,hash);
        return this.getOneOpt(query);
    }

    public List<TopTransaction> queryWithdrawUnConfirm() {
        LambdaQueryWrapper<TopTransaction> query = Wrappers.lambdaQuery();
        query.eq(TopTransaction::getIsConfirm,CommonStatus.UN_CONFIRM).eq(TopTransaction::getType, TransactionType.Withdraw)
                .isNotNull(TopTransaction::getHash);
        return this.list(query);
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
