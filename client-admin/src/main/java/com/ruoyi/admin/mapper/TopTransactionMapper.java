package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.admin.dto.TopTransactionDTO;
import com.ruoyi.admin.dto.TransactionStatisticDTO;
import com.ruoyi.admin.entity.TopTransaction;
import com.ruoyi.admin.vo.StatisticTransactionVO;
import com.ruoyi.admin.vo.TopTransactionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TopTransactionMapper extends BaseMapper<TopTransaction> {

    @Update("update top_transaction set is_confirm = 1 and `status`='0x1' where id = #{id}")
    void updateConfirm(Long id);

    @Update("update top_transaction set is_confirm = 2, `status`='0x2' where hash = #{hash}")
    void updateFailed(String hash);

    IPage<TopTransactionVO> selectPageVOByDTO(@Param("page") IPage<TopTransactionVO> page, @Param("dto") TopTransactionDTO dto);


    @Select("SELECT withdraw_amount, withdraw_created_date,withdraw_symbol,withdraw_operation,recharge_amount, recharge_created_date,recharge_symbol,recharge_operation from \n" +
            "(SELECT SUM(token_amount) withdraw_amount, created_date withdraw_created_date,symbol withdraw_symbol,'Withdraw' as withdraw_operation  FROM top_transaction tt where `type` LIKE '%Withdraw%' and status='0x1' GROUP BY created_date,symbol) withdraw\n" +
            "RIGHT JOIN \n" +
            "(SELECT SUM(token_amount) recharge_amount, created_date recharge_created_date,symbol recharge_symbol,'Recharge' as recharge_operation FROM top_transaction tt where `type` LIKE '%Recharge%' and status='0x1' GROUP BY created_date,symbol) recharge \n" +
            "on withdraw.withdraw_created_date = recharge.recharge_created_date and withdraw.withdraw_symbol = recharge.recharge_symbol")
    List<StatisticTransactionVO> getDayStatic(TransactionStatisticDTO transactionDayStatisticDTO);

    @Select("SELECT withdraw_amount,CONCAT(withdraw_year,'-',withdraw_month) withdraw_created_date,withdraw_symbol,withdraw_operation,recharge_amount,CONCAT(recharge_year,'-',recharge_month) recharge_created_date,recharge_symbol,recharge_operation FROM\n" +
            "(SELECT * FROM (SELECT SUM(token_amount) withdraw_amount,YEAR(created_date) withdraw_year, MONTH(created_date) withdraw_month,symbol withdraw_symbol,'Withdraw' withdraw_operation  FROM top_transaction tt where `type` LIKE '%Withdraw%' and status='0x1' GROUP BY YEAR(created_date), MONTH(created_date),symbol) withdraw\n" +
            "RIGHT JOIN\n" +
            "(SELECT SUM(token_amount) recharge_amount,YEAR(created_date) recharge_year, MONTH(created_date) recharge_month,symbol recharge_symbol,'Recharge' recharge_operation  FROM top_transaction tt where `type` LIKE '%Recharge%' and status='0x1'  GROUP BY YEAR(created_date), MONTH(created_date),symbol) recharge\n" +
            "on withdraw.withdraw_year = recharge.recharge_year and withdraw.withdraw_month = recharge.recharge_month and withdraw.withdraw_symbol = recharge.recharge_symbol) a\n")
    List<StatisticTransactionVO> getMonthStatic(TransactionStatisticDTO transactionDayStatisticDTO);
}
