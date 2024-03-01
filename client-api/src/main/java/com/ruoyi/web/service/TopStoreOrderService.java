package com.ruoyi.web.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.dto.AccountRequest;
import com.ruoyi.web.dto.StoreOrderDTO;
import com.ruoyi.web.dto.StoreOrderPageDTO;
import com.ruoyi.web.entity.TopStore;
import com.ruoyi.web.entity.TopStoreOrder;
import com.ruoyi.web.entity.TopToken;
import com.ruoyi.web.entity.TopUser;
import com.ruoyi.web.enums.Account;
import com.ruoyi.web.enums.Status;
import com.ruoyi.web.enums.TopNo;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.mapper.TopStoreMapper;
import com.ruoyi.web.mapper.TopStoreOrderMapper;
import com.ruoyi.web.mapper.TopUserMapper;
import com.ruoyi.web.vo.StoreOrderInfoVO;
import com.ruoyi.web.vo.PageVO;
import com.ruoyi.web.vo.StoreOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TopStoreOrderService extends ServiceImpl<TopStoreOrderMapper, TopStoreOrder> {

    @Autowired
    private TopStoreMapper storeMapper;

    @Autowired
    private TopUserMapper userMapper;

    @Autowired
    private TopAccountService accountService;

    @Autowired
    private TopTokenService topTokenService;

    /**
     * 存单信息
     */

    public StoreOrderInfoVO info(String walletAddress) {
        return baseMapper.selectInfoVO(walletAddress);
    }

    /**
     * 存单
     * 1.存入币种数量
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean order(String walletAddress, StoreOrderDTO dto) {
        TopStore store = storeMapper.selectById(dto.getStoreId());
        if (store.getStatus() != 1) {
            throw new ServiceException("状态错误", 500);
        }
        TopToken token = topTokenService.getBySymbol(dto.getSymbol());
        if (BooleanUtil.isFalse(token.getStoreEnabled())) {
            throw new ServiceException("当前币种无法进行存单", 500);
        }
        BigDecimal tokenPrice = token.getPrice();
        if (tokenPrice.compareTo(BigDecimal.ZERO) == 0) {
            throw new ServiceException("币种价格无法获取", 500);
        }
        TopUser user = userMapper.selectByWallet(walletAddress);
        String orderNo = TopNo.STORE_NO._code + IdUtil.getSnowflake(TopNo.STORE_NO._workId).nextIdStr();
        TopStoreOrder order = new TopStoreOrder();
        order.setStoreId(store.getId());
        order.setUserId(user.getId());
        order.setOrderNo(orderNo);
        order.setSymbol(dto.getSymbol());
        order.setAmount(dto.getAmount());
        order.setPrice(tokenPrice);
        order.setInvestAmount(order.getAmount().multiply(order.getPrice()));
        order.setOrderDate(LocalDate.now());
        order.setReleaseDate(LocalDate.now().plusMonths(store.getPeriod()));
        order.setStatus(Status._1._value);
        order.setCreatedBy(user.getId().toString());
        order.setCreatedDate(LocalDateTime.now());
        order.setUpdatedBy(user.getId().toString());
        order.setUpdatedDate(LocalDateTime.now());
        baseMapper.insert(order);

        accountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(UUID.fastUUID().toString().concat("_" + user.getId()).concat("_" + Account.TxType.STORE_IN.typeCode))
                                .userId(user.getId())
                                .token(order.getSymbol())
                                .balanceChanged(order.getAmount().negate())
                                .fee(BigDecimal.ZERO)
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.STORE_IN)
                                .refNo(orderNo)
                                .remark("存单")
                                .build()
                )
        );
        return true;
    }

    public PageVO<StoreOrderVO> getPage(String wallet, StoreOrderPageDTO dto) {
        IPage<StoreOrderVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectPageVO(iPage, wallet);
        PageVO<StoreOrderVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }

    /**
     * 理财生息
     * 1.按照订单每笔进行计算发放
     * 2.按照年利率得出日利率（利率/周期天）
     * 3.投资金额USD*（利率/周期），保留8位
     * 4.返给直接上级30%
     */
    @Transactional(rollbackFor = Exception.class)
    public void process(LocalDate processDate) {
        List<TopStoreOrder> storeOrderList = baseMapper.selectList(new LambdaQueryWrapper<TopStoreOrder>()
                .eq(TopStoreOrder::getStatus, Status._1._value)
                .ge(TopStoreOrder::getReleaseDate, processDate));
        Map<Long, List<TopStoreOrder>> orderMap = storeOrderList.stream().collect(Collectors.groupingByConcurrent(TopStoreOrder::getStoreId));
        orderMap.forEach((storeId, orders) -> {
            TopStore store = storeMapper.selectOne(new LambdaQueryWrapper<TopStore>().eq(TopStore::getId, storeId));
            for (TopStoreOrder order : orders) {
                TopStoreOrder lock = baseMapper.lockByOrderNo(order.getOrderNo());
                if (!Objects.equals(lock.getStatus(), Status._1._value)) {
                    continue;
                }
                TopUser user = userMapper.selectOne(new LambdaQueryWrapper<TopUser>().eq(TopUser::getId, lock.getUserId()));
                long days = lock.getOrderDate().until(lock.getReleaseDate(), ChronoUnit.DAYS);
                BigDecimal avgRate = store.getRate().divide(new BigDecimal(days), 8, RoundingMode.DOWN);
                BigDecimal income = lock.getInvestAmount().multiply(avgRate).setScale(8, RoundingMode.DOWN);
                List<AccountRequest> requests = new ArrayList<>();
                requests.add(
                        AccountRequest.builder()
                                .uniqueId(UUID.fastUUID().toString().concat("_" + lock.getUserId()).concat("_" + Account.TxType.STORE_INTEREST.typeCode))
                                .userId(lock.getUserId())
                                .token("USDT")
                                .balanceChanged(income)
                                .fee(BigDecimal.ZERO)
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.STORE_INTEREST)
                                .refNo(lock.getOrderNo())
                                .remark("利息")
                                .build()
                );
                // 收益返直接上级
                BigDecimal parentIncome = income.multiply(new BigDecimal("0.3")).setScale(8, RoundingMode.DOWN);
                requests.add(
                        AccountRequest.builder()
                                .uniqueId(UUID.fastUUID().toString().concat("_" + user.getInvitedUserId()).concat("_" + Account.TxType.STORE_INTEREST_INVITE.typeCode))
                                .userId(user.getInvitedUserId())
                                .token("USDT")
                                .balanceChanged(parentIncome)
                                .fee(BigDecimal.ZERO)
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.STORE_INTEREST_INVITE)
                                .refNo(lock.getOrderNo())
                                .remark("利息返上级")
                                .build()
                );
                // 到期自动赎回
                if (lock.getReleaseDate().isEqual(processDate)) {
                    requests.add(
                            AccountRequest.builder()
                                    .uniqueId(UUID.fastUUID().toString().concat("_" + lock.getUserId()).concat("_" + Account.TxType.STORE_REDEEM.typeCode))
                                    .userId(lock.getUserId())
                                    .token(lock.getSymbol())
                                    .balanceChanged(lock.getAmount())
                                    .fee(BigDecimal.ZERO)
                                    .balanceTxType(Account.Balance.AVAILABLE)
                                    .txType(Account.TxType.STORE_REDEEM)
                                    .refNo(lock.getOrderNo())
                                    .remark("赎回")
                                    .build()
                    );
                    lock.setStatus(Status._2._value);
                }
                accountService.processAccount(requests);
                lock.setUpdatedDate(LocalDateTime.now());
                lock.setUpdatedBy("SYS");
                baseMapper.updateById(lock);
            }
        });
    }

    public StoreOrderInfoVO getOderInfo(String wallet) {
        return baseMapper.selectInfoVO(wallet);
    }
}