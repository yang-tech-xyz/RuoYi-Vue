package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.dto.SettleDepositWithdrawPageDTO;
import com.ruoyi.admin.dto.SettleMemberInvitePageDTO;
import com.ruoyi.admin.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TopSettleMapper {

    SettleMemberCountVO selectMemberCount(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                          @Param("yesterdayStart") LocalDateTime yesterdayStart, @Param("yesterdayEnd") LocalDateTime yesterdayEnd);

    List<SettleDepositWithdrawVO> selectDepositWithdrawList(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    IPage<SettleDepositWithdrawPageVO> selectDepositWithdrawPageVO(@Param("iPage") IPage<SettleDepositWithdrawPageVO> iPage, @Param("dto") SettleDepositWithdrawPageDTO dto);

    IPage<SettleMemberInvitePageVO> selectMemberInvitePageVO(@Param("iPage") IPage<SettleMemberInvitePageVO> iPage, @Param("dto") SettleMemberInvitePageDTO dto);

    List<SettleDirectVO> selectDirectByUserId(@Param("userId") Long userId);

}
