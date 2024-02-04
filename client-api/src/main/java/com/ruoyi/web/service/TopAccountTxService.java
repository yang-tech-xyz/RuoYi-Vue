package com.ruoyi.web.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.dto.AccountTxPageDTO;
import com.ruoyi.web.dto.AccountTxRequest;
import com.ruoyi.web.dto.AccountTxRequestDetail;
import com.ruoyi.web.entity.TopAccount;
import com.ruoyi.web.entity.TopAccountTx;
import com.ruoyi.web.enums.Account;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.mapper.TopAccountMapper;
import com.ruoyi.web.mapper.TopAccountTxMapper;
import com.ruoyi.web.vo.AccountTxVO;
import com.ruoyi.web.vo.PageVO;
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
import java.util.stream.Collectors;

@Slf4j
@Service
public class TopAccountTxService extends ServiceImpl<TopAccountTxMapper, TopAccountTx> {

    @Autowired
    private TopAccountMapper accountMapper;

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
                tx.setMebId(a.getMebId());
                tx.setToken(lockAccount.getToken());
                tx.setUniqueId(a.getUniqueId());
                tx.setAmount(a.getBalanceChanged());
                tx.setFee(a.getFee());
                tx.setTransactionNo(txRequest.getTxNo());
                tx.setBalanceType(a.getBalanceTxType().typeCode);
                tx.setTxType(a.getTxType().typeCode);
                tx.setRefNo(a.getRefNo());
                tx.setRemark(a.getRemark());
                tx.setCreatedBy(String.valueOf(tx.getMebId()));
                tx.setCreatedDate(LocalDateTime.now());
                tx.setUpdatedBy(String.valueOf(tx.getMebId()));
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

    public PageVO<AccountTxVO> getPage(Long mebId, AccountTxPageDTO dto) {
        IPage<AccountTxVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectPageVO(iPage, mebId, dto);
        PageVO<AccountTxVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }
}
