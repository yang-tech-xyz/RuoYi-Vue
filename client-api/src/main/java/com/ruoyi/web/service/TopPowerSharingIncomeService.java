package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import java.util.stream.Collectors;

@Slf4j
@Service
public class TopPowerSharingIncomeService extends ServiceImpl<TopPowerSharingIncomeMapper, TopPowerSharingIncome> {

    @Autowired
    private TopPowerSharingConfigMapper sharingConfigMapper;

    /**
     * 层级收益
     * 1.按照已有矿机数量来获取层级数量
     * 2.
     */
    @Transactional(rollbackFor = Exception.class)
    public void process(List<UserProcessVO> userVOList, List<TopToken> tokens, LocalDate processDate) {
        List<PowerSharingConfigVO> configVOList = sharingConfigMapper.selectListVO();
        for (int i = 0; i < userVOList.size(); i++) {
            UserProcessVO userVO = userVOList.get(i);
            // 挖矿层级
            Integer lv = userVO.getPowerNumber();
            List<UserProcessVO> curChildMebList = userVO.getDirectChildMebList();
            for (int j = 1; j <= lv; j++) {
                for (int k = 0; k < curChildMebList.size(); k++) {
                    UserProcessVO curChild = curChildMebList.get(k);
                    for (String symbol : curChild.getDailyIncomeMap().keySet()) {
                        TopPowerSharingIncome sharingIncome = new TopPowerSharingIncome();
                        sharingIncome.setUserId(userVO.getId());
                        sharingIncome.setProviderUserId(curChild.getId());
                        sharingIncome.setProviderLevel(j);
                        sharingIncome.setIncomeSymbol(symbol);
                        sharingIncome.setProviderIncome(curChild.getDailyIncomeMap().getOrDefault(symbol, BigDecimal.ZERO));
                        sharingIncome.setIncomeRate(BigDecimal.ZERO);
                        for (PowerSharingConfigVO config : configVOList) {
                            if (config.getLevel() == j) {
                                sharingIncome.setIncomeRate(config.getRate());
                            }
                        }
                        sharingIncome.setIncome(sharingIncome.getProviderIncome().multiply(sharingIncome.getIncomeRate()));
                        sharingIncome.setIncomeDate(processDate);
                        sharingIncome.setProcessEnabled(Boolean.FALSE);
                        sharingIncome.setCreatedDate(LocalDateTime.now());
                        sharingIncome.setCreatedBy("SYS");
                        sharingIncome.setUpdatedDate(LocalDateTime.now());
                        sharingIncome.setUpdatedBy("SYS");
                        baseMapper.insert(sharingIncome);
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
