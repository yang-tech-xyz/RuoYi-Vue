package com.ruoyi.web.service;

import cn.hutool.core.lang.UUID;
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
import com.ruoyi.web.enums.Account;
import com.ruoyi.web.enums.Status;
import com.ruoyi.web.enums.TopNo;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.mapper.TopStoreMapper;
import com.ruoyi.web.mapper.TopStoreOrderMapper;
import com.ruoyi.web.vo.PageVO;
import com.ruoyi.web.vo.StoreOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TopStoreOrderService extends ServiceImpl<TopStoreOrderMapper, TopStoreOrder> {

    @Autowired
    private TopStoreMapper storeMapper;

    @Autowired
    private TopAccountService accountService;

    @Autowired
    private TopTokenPriceService topTokenPriceService;

    /**
     * 存单
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean order(Long mebId, StoreOrderDTO dto) {
        TopStore store = storeMapper.selectById(dto.getStoreId());
        if (store.getStatus() != 1) {
            throw new ServiceException("状态错误", 500);
        }
        if (dto.getAmount().compareTo(store.getMinOrderAmount()) < 0
                || dto.getAmount().compareTo(store.getMaxOrderAmount()) > 0) {
            throw new ServiceException("投注金额过小或过大", 500);
        }
        BigDecimal tokenPrice = topTokenPriceService.getPrice(store.getSymbol());
        if (tokenPrice.compareTo(BigDecimal.ZERO) == 0) {
            throw new ServiceException("币种价格无法获取", 500);
        }
        String orderNo = TopNo.STORE_NO._code + IdUtil.getSnowflake(TopNo.STORE_NO._workId).nextIdStr();
        TopStoreOrder order = new TopStoreOrder();
        order.setStoreId(store.getId());
        order.setMebId(mebId);
        order.setOrderNo(orderNo);
        order.setSymbol(store.getSymbol());
        order.setIncomeSymbol(store.getIncomeSymbol());
        order.setPrice(tokenPrice);
        order.setAmount(dto.getAmount());
        order.setRate(store.getRate());
        order.setIncome(order.getAmount().multiply(order.getPrice()).multiply(order.getRate()));
        order.setStoreDate(LocalDateTime.now());
        order.setReleaseDate(LocalDate.now().plusDays(store.getPeriod() * 30));
        order.setStatus(Status._1.value);
        order.setCreatedBy(mebId.toString());
        order.setCreatedDate(LocalDateTime.now());
        order.setUpdatedBy(mebId.toString());
        order.setUpdatedDate(LocalDateTime.now());
        baseMapper.insert(order);

        accountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(UUID.fastUUID().toString().concat("_" + mebId).concat("_" + Account.TxType.STORE_IN.typeCode))
                                .mebId(mebId)
                                .token(store.getSymbol())
                                .fee(BigDecimal.ZERO)
                                .balanceChanged(dto.getAmount().negate())
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.STORE_IN)
                                .remark("存单")
                                .build()
                )
        );
        return true;
    }

    public PageVO<StoreOrderVO> getPage(Long mebId, StoreOrderPageDTO dto) {
        IPage<StoreOrderVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectPageVO(iPage, mebId);
        PageVO<StoreOrderVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }

    /**
     * 赎回
     * 1.本金退回，利息发放
     * 2.达到赎回时间以后的订单
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean redeem(Long mebId) {
        LocalDate now = LocalDate.now();
        List<TopStoreOrder> storeOrders = baseMapper.selectList(new LambdaQueryWrapper<TopStoreOrder>()
                .eq(TopStoreOrder::getMebId, mebId)
                .eq(TopStoreOrder::getStatus, Status._1.value)
                .ge(TopStoreOrder::getReleaseDate, now));
        if (storeOrders.isEmpty()) {
            throw new ServiceException("没有可赎回的订单", 500);
        }
        List<AccountRequest> requests = new ArrayList<>();
        for (TopStoreOrder storeOrder : storeOrders) {
            TopStoreOrder lock = baseMapper.lockByOrderNo(storeOrder.getOrderNo());
            if (!Objects.equals(lock.getStatus(), Status._1.value)) {
                continue;
            }
            if (storeOrder.getReleaseDate().isBefore(now)) {
                continue;
            }
            lock.setStatus(Status._2.value);
            lock.setRedeemDate(LocalDateTime.now());
            baseMapper.updateById(lock);
            requests.add(
                    AccountRequest.builder()
                            .uniqueId(UUID.fastUUID().toString().concat("_" + mebId).concat("_" + Account.TxType.STORE_REDEEM.typeCode))
                            .mebId(mebId)
                            .token(lock.getSymbol())
                            .fee(BigDecimal.ZERO)
                            .balanceChanged(lock.getAmount())
                            .balanceTxType(Account.Balance.AVAILABLE)
                            .txType(Account.TxType.STORE_REDEEM)
                            .remark("本金赎回")
                            .build()
            );
            requests.add(
                    AccountRequest.builder()
                            .uniqueId(UUID.fastUUID().toString().concat("_" + mebId).concat("_" + Account.TxType.STORE_REDEEM_INTEREST.typeCode))
                            .mebId(mebId)
                            .token(lock.getIncomeSymbol())
                            .fee(BigDecimal.ZERO)
                            .balanceChanged(lock.getIncome())
                            .balanceTxType(Account.Balance.AVAILABLE)
                            .txType(Account.TxType.STORE_REDEEM_INTEREST)
                            .remark("赎回利息")
                            .build()
            );
        }
        if (!requests.isEmpty()) {
            accountService.processAccount(
                    requests
            );
        }
        return true;
    }
}
