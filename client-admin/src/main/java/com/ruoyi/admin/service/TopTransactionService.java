package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.common.CommonStatus;
import com.ruoyi.admin.dto.TopTransactionDTO;
import com.ruoyi.admin.dto.TransactionStatisticDTO;
import com.ruoyi.admin.dto.UnAuditTopTransactionDTO;
import com.ruoyi.admin.entity.TopTransaction;
import com.ruoyi.admin.enums.TransactionType;
import com.ruoyi.admin.exception.ServiceException;
import com.ruoyi.admin.mapper.TopTransactionMapper;
import com.ruoyi.admin.vo.StatisticTransactionVO;
import com.ruoyi.admin.vo.TopTransactionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class TopTransactionService extends ServiceImpl<TopTransactionMapper, TopTransaction> {

    /**
     * 事务确认
     *
     * @param hash
     */
    public void updateFailed(String hash) {
        this.baseMapper.updateFailed(hash);
    }

    public TopTransaction getOptByTransactionNo(String transactionNo) {
        LambdaQueryWrapper<TopTransaction> query = Wrappers.lambdaQuery();
        query.eq(TopTransaction::getTransNo, transactionNo);
        Optional<TopTransaction> topTransaction = this.getOneOpt(query);
        if (topTransaction.isEmpty()) {
            log.warn("transaction is not exist,transactionNo is:{}", transactionNo);
            throw new ServiceException("transaction is not exist");
        }
        return topTransaction.get();
    }

//    public void updateConfirm(TopTransaction topTransaction) {
//        this.baseMapper.updateConfirm(topTransaction.getId());
//    }

    public Optional<TopTransaction> getTransactionByHash(String hash) {
        LambdaQueryWrapper<TopTransaction> query = Wrappers.lambdaQuery();
        query.eq(TopTransaction::getHash, hash);
        return this.getOneOpt(query);
    }

    public List<TopTransaction> queryWithdrawUnConfirm() {
        LambdaQueryWrapper<TopTransaction> query = Wrappers.lambdaQuery();
        query.eq(TopTransaction::getIsConfirm, CommonStatus.UN_CONFIRM).eq(TopTransaction::getType, TransactionType.Withdraw).ne(TopTransaction::getChainId, -1)
                .isNotNull(TopTransaction::getHash);
        return this.list(query);
    }

    public List<TopTransaction> queryTronWithdrawUnConfirm() {
        LambdaQueryWrapper<TopTransaction> query = Wrappers.lambdaQuery();
        query.eq(TopTransaction::getIsConfirm, CommonStatus.UN_CONFIRM).eq(TopTransaction::getType, TransactionType.Tron_Withdraw).eq(TopTransaction::getChainId, -1)
                .isNotNull(TopTransaction::getHash);
        return this.list(query);
    }

    public IPage<TopTransactionVO> getTransaction(TopTransactionDTO dto) {
        IPage<TopTransactionVO> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.selectPageVOByDTO(page, dto);
    }

    public List<TopTransaction> getUnAuditTransaction(UnAuditTopTransactionDTO unAuditTopTransactionDTO) {
        LambdaQueryWrapper<TopTransaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNull(TopTransaction::getHash)
                .eq(TopTransaction::getStatus, CommonStatus.STATES_COMMIT)
                .eq(TopTransaction::getType, TransactionType.Withdraw)
                .eq((unAuditTopTransactionDTO.getChainId() != null), TopTransaction::getChainId, unAuditTopTransactionDTO.getChainId())
                .orderByDesc(TopTransaction::getCreateTime);
        return this.getBaseMapper().selectList(wrapper);
    }

    public List<StatisticTransactionVO> getDayStatic(TransactionStatisticDTO transactionStatisticDTO) {
        return this.getBaseMapper().getDayStatic(transactionStatisticDTO);
    }

    public List<StatisticTransactionVO> getMonthStatic(TransactionStatisticDTO transactionStatisticDTO) {
        return this.getBaseMapper().getMonthStatic(transactionStatisticDTO);
    }
}
