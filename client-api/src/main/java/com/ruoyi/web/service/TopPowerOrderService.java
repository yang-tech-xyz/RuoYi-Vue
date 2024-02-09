package com.ruoyi.web.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.dto.AccountRequest;
import com.ruoyi.web.dto.PowerOrderPageDTO;
import com.ruoyi.web.entity.TopAccount;
import com.ruoyi.web.entity.TopPowerConfig;
import com.ruoyi.web.entity.TopPowerOrder;
import com.ruoyi.web.entity.TopUserEntity;
import com.ruoyi.web.enums.Account;
import com.ruoyi.web.enums.TopNo;
import com.ruoyi.web.exception.ServiceException;
import com.ruoyi.web.mapper.TopPowerOrderMapper;
import com.ruoyi.web.vo.BuyPowerBody;
import com.ruoyi.web.vo.PageVO;
import com.ruoyi.web.vo.PowerOrderVO;
import com.ruoyi.web.vo.UserProcessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TopPowerOrderService extends ServiceImpl<TopPowerOrderMapper, TopPowerOrder> {

    @Autowired
    private TopPowerConfigService topPowerConfigService;

    @Autowired
    private TopTokenPriceService topTokenPriceService;

    @Autowired
    private TopUserService topUserService;

    @Autowired
    private TopAccountService topAccountService;


    @Transactional
    public void buyOrder(BuyPowerBody buyPowerBody) {
        TopPowerOrder topPowerOrder = new TopPowerOrder();
        List<TopPowerConfig> powerConfigs = topPowerConfigService.list();
        if (powerConfigs == null || powerConfigs.size() == 0) {
            throw new ServiceException("powerConfig is error!");
        }
        TopPowerConfig topPowerConfig = powerConfigs.get(0);
        //通过购买数量,计算需要的金额.
        BigDecimal price = topPowerConfig.getPrice();
        BigDecimal buyNumbers = buyPowerBody.getNumber();
        topPowerOrder.setNumber(buyNumbers.intValue());
        // 购买矿机需要的U的数量
        BigDecimal buyPowerNeedPayUsdt = buyNumbers.multiply(price);
        topPowerOrder.setAmount(buyPowerNeedPayUsdt);
        topPowerOrder.setOutputSymbol(topPowerConfig.getOutputSymbol());
        topPowerOrder.setPeriod(topPowerConfig.getPeriod());
        topPowerOrder.setOutputRatio(topPowerConfig.getOutputRatio());
        topPowerOrder.setEndDate(LocalDate.now().plusDays(topPowerOrder.getPeriod() + 1));
        topPowerOrder.setCreateTime(LocalDateTime.now());
        topPowerOrder.setUpdateTime(LocalDateTime.now());
        topPowerOrder.setExpectedTotalOutput(buyPowerNeedPayUsdt.multiply(topPowerConfig.getOutputRatio()));
        String symbol = buyPowerBody.getSymbol();
        // 查询symbol的价格.
        BigDecimal tokenPrice = topTokenPriceService.getPrice(symbol);
        // 计算用户需要的token的数量
        BigDecimal payTokenAmount = buyPowerNeedPayUsdt.divide(tokenPrice, 10, 2);
        String wallet = buyPowerBody.getWallet().toLowerCase();
        Optional<TopUserEntity> topUserOptional = topUserService.getByWallet(wallet);
        if (!topUserOptional.isPresent()) {
            log.warn("user not exist,wallet is:{}", wallet);
            throw new ServiceException("user not exist,wallet");
        }
        TopUserEntity topUserEntity = topUserOptional.get();
        TopAccount account = topAccountService.getAccount(topUserEntity.getId().longValue(), symbol);
        if (account.getAvailableBalance().compareTo(payTokenAmount) < 0) {
            log.warn("account have no enough money,account info:{}", account);
        }
        String orderNo = TopNo.POWER_NO._code + IdUtil.getSnowflake(TopNo.POWER_NO._workId).nextIdStr();
        topPowerOrder.setOrderNo(orderNo);
        Long userId = account.getUserId();
        topPowerOrder.setUserId(userId);
        topPowerOrder.setCreateBy(userId.toString());
        topPowerOrder.setUpdateBy(userId.toString());
        // 扣减用户购买算力的钱
        topAccountService.processAccount(
                Arrays.asList(
                        AccountRequest.builder()
                                .uniqueId(orderNo.concat("_" + userId).concat("_" + Account.TxType.PURCHASE.typeCode))
                                .userId(userId)
                                .token(symbol)
                                .fee(BigDecimal.ZERO)
                                .balanceChanged(payTokenAmount.negate())
                                .balanceTxType(Account.Balance.AVAILABLE)
                                .txType(Account.TxType.PURCHASE)
                                .refNo(orderNo)
                                .remark("支付")
                                .build()
                )
                // TODO 是否需要記錄用戶系統賬戶入賬?
        );

        //生成算力訂單
        this.baseMapper.insert(topPowerOrder);
    }

    /**
     * 处理订单
     */
    public void process(List<UserProcessVO> userVOList, LocalDate processDate) {
        for (int i = 0; i < userVOList.size(); i++) {
            UserProcessVO userVO = userVOList.get(i);
            userVO.setPowerOrders(baseMapper.selectOrderList(userVO.getId(), processDate));
            userVO.setPowerNumber(userVO.getPowerOrders().stream().map(TopPowerOrder::getNumber).mapToInt(num -> num).sum());
        }
    }

    public PageVO<PowerOrderVO> getPage(String walletAddress, PowerOrderPageDTO dto) {
        IPage<PowerOrderVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        iPage = baseMapper.selectPageVO(iPage, walletAddress);
        PageVO<PowerOrderVO> pageVO = new PageVO<>();
        pageVO.setPageNum(dto.getPageNum());
        pageVO.setPageSize(dto.getPageSize());
        pageVO.setTotal(iPage.getTotal());
        pageVO.setList(iPage.getRecords());
        return pageVO;
    }
}
