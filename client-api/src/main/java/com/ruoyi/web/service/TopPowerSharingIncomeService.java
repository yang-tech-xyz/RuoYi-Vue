package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.entity.TopPowerOrderIncome;
import com.ruoyi.web.entity.TopPowerSharingIncome;
import com.ruoyi.web.entity.TopToken;
import com.ruoyi.web.mapper.TopPowerSharingConfigMapper;
import com.ruoyi.web.mapper.TopPowerSharingIncomeMapper;
import com.ruoyi.web.vo.PowerSharingConfigVO;
import com.ruoyi.web.vo.UserProcessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TopPowerSharingIncomeService extends ServiceImpl<TopPowerSharingIncomeMapper, TopPowerSharingIncome> {

    @Autowired
    private TopPowerSharingConfigMapper sharingConfigMapper;

    /**
     * 层级收益
     * 1.按照已有矿机数量来获取层级数量
     * 2.每层用户只能获取到本身台数数量收益，按照订单时间正序
     * 3.收益：用户挖矿收益BTC*层级比例
     */
    @Transactional(rollbackFor = Exception.class)
    public void process(List<UserProcessVO> userVOList, Map<String, TopToken> tokens, LocalDate processDate) {
        List<PowerSharingConfigVO> configVOList = sharingConfigMapper.selectListVO();
        for (int i = 0; i < userVOList.size(); i++) {
            UserProcessVO userVO = userVOList.get(i);
            // 挖矿层级,最大十层
            int lv = Math.min(userVO.getPowerNumber(), 10);
            List<UserProcessVO> curChildMebList = userVO.getDirectChildMebList();
            for (int j = 1; j <= lv; j++) {
                // 层级利率
                BigDecimal levelRate = BigDecimal.ZERO;
                for (PowerSharingConfigVO config : configVOList) {
                    if (config.getLevel() == j) {
                        levelRate = config.getRate();
                    }
                }
                for (int k = 0; k < curChildMebList.size(); k++) {
                    UserProcessVO curChild = curChildMebList.get(k);
                    int powerNumber = userVO.getPowerNumber();
                    // 下级订单，要根据父级拥有的台数，按照订单顺序进行返利
                    for (int l = 0; l < curChild.getIncomeOrders().size(); l++) {
                        if (powerNumber <= 0) {
                            break;
                        }
                        TopPowerOrderIncome incomeOrder = curChild.getIncomeOrders().get(l);
                        TopPowerSharingIncome sharingIncome = new TopPowerSharingIncome();
                        sharingIncome.setUserId(userVO.getId());
                        sharingIncome.setProviderUserId(curChild.getId());
                        sharingIncome.setProviderOrderNo(incomeOrder.getOrderNo());
                        sharingIncome.setProviderLevel(j);
                        sharingIncome.setRate(levelRate);
                        sharingIncome.setSymbol(incomeOrder.getSymbol());
                        sharingIncome.setIncome(incomeOrder.getIncome().multiply(sharingIncome.getRate()));
                        sharingIncome.setProcessEnabled(Boolean.FALSE);
                        sharingIncome.setProcessDate(processDate);
                        sharingIncome.setCreatedDate(LocalDateTime.now());
                        sharingIncome.setCreatedBy("SYS");
                        sharingIncome.setUpdatedDate(LocalDateTime.now());
                        sharingIncome.setUpdatedBy("SYS");
                        baseMapper.insert(sharingIncome);
                        powerNumber = powerNumber - 1;
                    }
                }
                // 下一层数据
                if (j < lv) {
                    curChildMebList = curChildMebList.stream()
                            .map(UserProcessVO::getDirectChildMebList)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList());
                }
            }
        }

    }
}
