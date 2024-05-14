package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.dto.SettleDepositWithdrawPageDTO;
import com.ruoyi.admin.vo.SettleDepositWithdrawPageVO;
import com.ruoyi.admin.vo.SettleDepositWithdrawVO;
import com.ruoyi.admin.vo.SettleMemberCountVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TopSettleMapper {

    SettleMemberCountVO selectMemberCount(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                          @Param("yesterdayStart") LocalDateTime yesterdayStart, @Param("yesterdayEnd") LocalDateTime yesterdayEnd);

    List<SettleDepositWithdrawVO> selectDepositWithdrawList(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    IPage<SettleDepositWithdrawPageVO> selectPageVO(@Param("iPage") IPage<SettleDepositWithdrawPageVO> iPage, @Param("dto") SettleDepositWithdrawPageDTO dto);

}
