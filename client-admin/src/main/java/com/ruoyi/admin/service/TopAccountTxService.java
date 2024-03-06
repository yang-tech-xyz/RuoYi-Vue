package com.ruoyi.admin.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.dto.AccountTxPageDTO;
import com.ruoyi.admin.dto.AccountTxRequest;
import com.ruoyi.admin.dto.AccountTxRequestDetail;
import com.ruoyi.admin.dto.StoreIncomePageDTO;
import com.ruoyi.admin.entity.TopAccount;
import com.ruoyi.admin.entity.TopAccountTx;
import com.ruoyi.admin.enums.Account;
import com.ruoyi.admin.exception.ServiceException;
import com.ruoyi.admin.mapper.TopAccountMapper;
import com.ruoyi.admin.mapper.TopAccountTxMapper;
import com.ruoyi.admin.vo.AccountTxVO;
import com.ruoyi.admin.vo.PageVO;
import com.ruoyi.admin.vo.StoreIncomeVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TopAccountTxService extends ServiceImpl<TopAccountTxMapper, TopAccountTx> {

    @Autowired
    private TopAccountMapper accountMapper;

    public PageVO<AccountTxVO> getPage(AccountTxPageDTO dto) {
        IPage<AccountTxVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectPageVO(iPage, dto);
        PageVO<AccountTxVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }

    public TopAccountTx getByRefNo(String transNo) {
        LambdaQueryWrapper<TopAccountTx> query = Wrappers.lambdaQuery();
        query.eq(TopAccountTx::getRefNo, transNo);
        Optional<TopAccountTx> topAccountTxOptional = this.getOneOpt(query);
        if (topAccountTxOptional.isEmpty()) {
            log.error("account tx is not exist,refer no is:" + transNo);
            throw new ServiceException("account tx is not exist");
        }
        return topAccountTxOptional.get();
    }

    /**
     * 流水数量
     */
    public Long checkUniqueId(String uniqueId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<TopAccountTx>().eq(TopAccountTx::getUniqueId, uniqueId));
    }


    @Transactional(rollbackFor = Exception.class)
    public void processedTx(AccountTxRequest txRequest) {
        log.info("【TOP - API】 ->  资产处理,txRequest：{}", txRequest);
        Map<Long, List<AccountTxRequestDetail>> txMap = txRequest.getDetails().stream().collect(Collectors.groupingBy(AccountTxRequestDetail::getAccountId));
        txMap.forEach((accountId, accounts) -> {
            TopAccount lockAccount = accountMapper.lockById(accountId);
            Assert.notNull(lockAccount, "invalid accountId");
            accounts.forEach(a -> {
                TopAccountTx tx = new TopAccountTx();
                if (StringUtils.equals(a.getBalanceTxType().typeCode, Account.Balance.AVAILABLE.typeCode)) {
                    tx.setBalanceBefore(lockAccount.getAvailableBalance());
                    lockAccount.setAvailableBalance(lockAccount.getAvailableBalance().add(a.getBalanceChanged()).add(a.getFee()));
                    tx.setBalanceAfter(lockAccount.getAvailableBalance());
                }
                if (StringUtils.equals(a.getBalanceTxType().typeCode, Account.Balance.FROZEN.typeCode)) {
                    tx.setBalanceBefore(lockAccount.getFrozenBalance());
                    lockAccount.setFrozenBalance(lockAccount.getFrozenBalance().add(a.getBalanceChanged()).add(a.getFee()));
                    tx.setBalanceAfter(lockAccount.getFrozenBalance());
                }
                if (StringUtils.equals(a.getBalanceTxType().typeCode, Account.Balance.LOCKUP.typeCode)) {
                    tx.setBalanceBefore(lockAccount.getLockupBalance());
                    lockAccount.setLockupBalance(lockAccount.getLockupBalance().add(a.getBalanceChanged()).add(a.getFee()));
                    tx.setBalanceAfter(lockAccount.getLockupBalance());
                }
                tx.setAccountId(accountId);
                tx.setUserId(a.getUserId());
                tx.setSymbol(lockAccount.getSymbol());
                tx.setUniqueId(a.getUniqueId());
                tx.setAmount(a.getBalanceChanged());
                tx.setFee(a.getFee());
                tx.setTransactionNo(txRequest.getTxNo());
                tx.setBalanceType(a.getBalanceTxType().typeCode);
                tx.setTxType(a.getTxType().typeCode);
                tx.setRefNo(a.getRefNo());
                tx.setRemark(a.getRemark());
                tx.setCreatedBy(String.valueOf(tx.getUserId()));
                tx.setCreatedDate(LocalDateTime.now());
                tx.setUpdatedBy(String.valueOf(tx.getUserId()));
                tx.setUpdatedDate(LocalDateTime.now());
                baseMapper.insert(tx);
            });
            checkBalance(lockAccount.getAvailableBalance(), lockAccount.getFrozenBalance(), lockAccount.getLockupBalance());
            accountMapper.updateById(lockAccount);
        });
    }


    public void checkBalance(BigDecimal... balance) {
        if (Arrays.stream(balance).anyMatch(e -> e.compareTo(BigDecimal.ZERO) < 0)) {
            throw new ServiceException("余额不足", 500);
        }
    }

    public PageVO<StoreIncomeVO> getStoreIncomePage(StoreIncomePageDTO dto) {
        IPage<StoreIncomeVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectStoreIncomePageVO(iPage, dto);
        PageVO<StoreIncomeVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }
}
