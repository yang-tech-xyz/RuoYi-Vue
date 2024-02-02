package com.ruoyi.web.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.dto.AccountRequest;
import com.ruoyi.web.dto.StoreOrderDTO;
import com.ruoyi.web.dto.StoreOrderPageDTO;
import com.ruoyi.web.entity.TopStore;
import com.ruoyi.web.entity.TopStoreOrder;
import com.ruoyi.web.enums.Account;
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
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class TopStoreOrderService extends ServiceImpl<TopStoreOrderMapper, TopStoreOrder> {

    @Autowired
    private TopStoreMapper storeMapper;

    @Autowired
    private TopAccountService accountService;

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

        String orderNo = TopNo.STORE_NO._code + IdUtil.getSnowflake(TopNo.STORE_NO._workId).nextIdStr();
        TopStoreOrder order = new TopStoreOrder();
        order.setStoreId(store.getId());
        order.setMebId(mebId);
        order.setOrderNo(orderNo);
        order.setToken(store.getToken());
        order.setAmount(dto.getAmount());
        order.setStoreDate(LocalDateTime.now());
        order.setReleaseDate(LocalDateTime.now().plusMonths(store.getPeriod()));
        order.setCreatedBy(mebId.toString());
        order.setCreatedDate(LocalDateTime.now());
        order.setUpdatedBy(mebId.toString());
        order.setUpdatedDate(LocalDateTime.now());
        baseMapper.insert(order);

        accountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(UUID.fastUUID().toString().concat("_" + String.valueOf(mebId)).concat("_" + Account.TxType.STORE_IN.typeCode))
                                .mebId(mebId)
                                .symbol(store.getToken())
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
}
