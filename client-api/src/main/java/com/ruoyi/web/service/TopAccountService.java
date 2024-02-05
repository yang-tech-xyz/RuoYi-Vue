package com.ruoyi.web.service;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.dto.AccountRequest;
import com.ruoyi.web.dto.AccountTxRequest;
import com.ruoyi.web.dto.AccountTxRequestDetail;
import com.ruoyi.web.entity.TopAccount;
import com.ruoyi.web.mapper.TopAccountMapper;
import com.ruoyi.web.vo.AccountVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TopAccountService extends ServiceImpl<TopAccountMapper, TopAccount> {

    @Autowired
    private TopAccountTxService txService;

    @Transactional
    public void processAccount(List<AccountRequest> requests) {
        log.info("【TOP - API】 -> 资产处理:{}", requests);
        AccountTxRequest txRequest = new AccountTxRequest();
        txRequest.setTxNo(UUID.fastUUID().toString(true));
        requests.stream().filter(e -> e.getBalanceChanged().compareTo(BigDecimal.ZERO) != 0).forEach(e -> {
            txRequest.addDetail(AccountTxRequestDetail.builder()
                    .uniqueId(e.getUniqueId())
                    .accountId(getAccount(e.getMebId(), e.getToken()).getId())
                    .mebId(e.getMebId())
                    .token(e.getToken())
                    .fee(e.getFee())
                    .balanceChanged(e.getBalanceChanged())
                    .balanceTxType(e.getBalanceTxType())
                    .txType(e.getTxType())
                    .refNo(e.getRefNo())
                    .remark(e.getRemark()).build());
        });
        if (!txRequest.getDetails().isEmpty()) {
            txService.processedTx(txRequest);
        }
    }

    public TopAccount getAccount(Long mebId, String token) {
        Optional<TopAccount> optional = Optional.ofNullable(baseMapper.selectOne(new LambdaQueryWrapper<TopAccount>()
                .eq(TopAccount::getMebId, mebId).eq(TopAccount::getSymbol, token)));
        if (optional.isPresent()) {
            return optional.get();
        }
        TopAccount account = new TopAccount();
        account.setMebId(mebId);
        account.setSymbol(token);
        account.setAvailableBalance(BigDecimal.ZERO);
        account.setLockupBalance(BigDecimal.ZERO);
        account.setFrozenBalance(BigDecimal.ZERO);
        account.setCreatedDate(LocalDateTime.now());
        account.setCreatedBy(String.valueOf(mebId));
        account.setUpdatedDate(LocalDateTime.now());
        account.setUpdatedBy(String.valueOf(mebId));
        baseMapper.insert(account);
        return account;
    }

    /**
     * 获取用户所有资产
     */
    public List<AccountVO> getAccounts(Long mebId) {
        List<TopAccount> accounts = baseMapper.selectList(new LambdaQueryWrapper<TopAccount>()
                .eq(TopAccount::getMebId, mebId));
        return accounts.stream().map(account -> {
            AccountVO vo = new AccountVO();
            vo.setMebId(account.getMebId());
            vo.setSymbol(account.getSymbol());
            vo.setAvailableBalance(account.getAvailableBalance());
            vo.setLockupBalance(account.getLockupBalance());
            vo.setFrozenBalance(account.getFrozenBalance());
            return vo;
        }).collect(Collectors.toList());
    }
}
