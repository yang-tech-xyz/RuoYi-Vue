package com.ruoyi.admin.service;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.dto.AccountPageDTO;
import com.ruoyi.admin.dto.AccountRequest;
import com.ruoyi.admin.dto.AccountTxRequest;
import com.ruoyi.admin.dto.AccountTxRequestDetail;
import com.ruoyi.admin.entity.TopAccount;
import com.ruoyi.admin.entity.TopAccountTx;
import com.ruoyi.admin.enums.Account;
import com.ruoyi.admin.mapper.TopAccountMapper;
import com.ruoyi.admin.vo.AccountVO;
import com.ruoyi.admin.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TopAccountService extends ServiceImpl<TopAccountMapper, TopAccount> {

    @Autowired
    private TopAccountTxService topAccountTxService;

    @Autowired
    private TopAccountService topAccountService;

    public PageVO<AccountVO> getPage(AccountPageDTO dto) {
        IPage<AccountVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectPageVO(iPage, dto);
        PageVO<AccountVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }

    @Transactional
    public void refund(String transNo) {
        //查询对应的流水
        TopAccountTx topAccountTx = topAccountTxService.getByRefNo(transNo);
        String uuid = UUID.fastUUID().toString();
        Long userId = topAccountTx.getUserId();
        String symbol = topAccountTx.getSymbol();
        BigDecimal amount = topAccountTx.getAmount();
        BigDecimal fee = topAccountTx.getFee();
        //退回用户的资金
        topAccountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(uuid.concat("_" + userId).concat("_" + Account.TxType.WITHDRAW_REJECT.typeCode))
                                .userId(userId)
                                .token(symbol)
                                .balanceChanged(amount)
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.WITHDRAW_REJECT)
                                .refNo(transNo)
                                .remark("提现")
                                .build(),
                        AccountRequest.builder()
                                .uniqueId(uuid.concat("_" + userId).concat("_" + Account.TxType.WITHDRAW_REJECT.typeCode))
                                .userId(userId)
                                .token(symbol)
                                .balanceChanged(fee)
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.WITHDRAW_REJECT)
                                .refNo(transNo)
                                .remark("提现")
                                .build()
                )
        );
    }

    public void processAccount(List<AccountRequest> requests) {
        log.info("【TOP - API】 -> 资产处理:{}", requests);
        AccountTxRequest txRequest = new AccountTxRequest();
        txRequest.setTxNo(UUID.fastUUID().toString(true));
        requests.stream()
                .filter(e -> e.getBalanceChanged().compareTo(BigDecimal.ZERO) != 0)
                .filter(e -> checkUniqueId(e.getUniqueId()) == 0)
                .forEach(e -> {
                    txRequest.addDetail(AccountTxRequestDetail.builder()
                            .uniqueId(e.getUniqueId())
                            .accountId(getAccount(e.getUserId(), e.getToken()).getId())
                            .userId(e.getUserId())
                            .token(e.getToken())
                            .fee(e.getFee())
                            .balanceChanged(e.getBalanceChanged())
                            .balanceTxType(e.getBalanceTxType())
                            .txType(e.getTxType())
                            .refNo(e.getRefNo())
                            .remark(e.getRemark()).build());
                });
        if (!txRequest.getDetails().isEmpty()) {
            topAccountTxService.processedTx(txRequest);
        }
    }

    public TopAccount getAccount(Long mebId, String token) {
        Optional<TopAccount> optional = Optional.ofNullable(baseMapper.selectOne(new LambdaQueryWrapper<TopAccount>()
                .eq(TopAccount::getUserId, mebId).eq(TopAccount::getSymbol, token)));
        if (optional.isPresent()) {
            return optional.get();
        }
        TopAccount account = new TopAccount();
        account.setUserId(mebId);
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


    private Long checkUniqueId(String uniqueId) {
        return topAccountTxService.checkUniqueId(uniqueId);
    }

}
