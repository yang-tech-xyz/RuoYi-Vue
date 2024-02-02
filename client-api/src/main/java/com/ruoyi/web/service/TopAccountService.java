package com.ruoyi.web.service;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.dto.AccountRequest;
import com.ruoyi.web.dto.AccountTxRequest;
import com.ruoyi.web.dto.AccountTxRequestDetail;
import com.ruoyi.web.entity.TopAccount;
import com.ruoyi.web.mapper.TopAccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TopAccountService extends ServiceImpl<TopAccountMapper, TopAccount> {

    @Autowired
    private TopAccountTxService txService;

    public void processAccount(List<AccountRequest> requests) {
        log.info("【TOP - API】 -> 资产处理:{}", requests);
        AccountTxRequest txRequest = new AccountTxRequest();
        txRequest.setTxNo(UUID.fastUUID().toString(true));
        requests.stream().filter(e -> e.getBalanceChanged().compareTo(BigDecimal.ZERO) != 0).forEach(e -> {
            txRequest.addDetail(AccountTxRequestDetail.builder()
                    .uniqueId(e.getUniqueId())
                    .accountId(getAccount(e.getMebId(), e.getSymbol()).getId())
                    .mebId(e.getMebId())
                    .symbol(e.getSymbol())
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

    public TopAccount getAccount(Long mebId, String symbol) {
        Optional<TopAccount> optional = Optional.ofNullable(baseMapper.selectOne(new LambdaQueryWrapper<TopAccount>()
                .eq(TopAccount::getMebId, mebId).eq(TopAccount::getSymbol, symbol)));
        if (optional.isPresent()) {
            return optional.get();
        }
        TopAccount account = new TopAccount();
        account.setMebId(mebId);
        account.setSymbol(symbol);
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
}
