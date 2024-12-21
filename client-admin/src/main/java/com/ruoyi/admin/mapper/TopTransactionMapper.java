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


    @Select("SELECT IFNULL(withdraw_amount,0) withdraw_amount,IFNULL(recharge_amount,0)recharge_amount,IFNULL(withdraw_symbol,recharge_symbol) as symbol, operate_date from (\n" +
            "SELECT withdraw_amount, withdraw_created_date,withdraw_symbol,withdraw_operation,recharge_amount, recharge_created_date,recharge_symbol,recharge_operation,IFNULL(withdraw_created_date,recharge_created_date) as operate_date from (\n" +
            "SELECT withdraw_amount, withdraw_created_date,withdraw_symbol,withdraw_operation,recharge_amount, recharge_created_date,recharge_symbol,recharge_operation from\n" +
            "(\n" +
            "\tSELECT SUM(token_amount) withdraw_amount, created_date withdraw_created_date,symbol withdraw_symbol,'Withdraw' as withdraw_operation  \n" +
            "\t\tFROM top_transaction tt where `type` LIKE '%Withdraw%' AND symbol <> 'BTC' and status='0x1' GROUP BY created_date,symbol\n" +
            "\tUNION \n" +
            "\tSELECT SUM(token_amount) withdraw_amount, created_date withdraw_created_date,symbol withdraw_symbol,'Withdraw' as withdraw_operation  \n" +
            "\t\tFROM top_transaction tt where `type` LIKE '%Withdraw%' AND symbol = 'BTC' GROUP BY created_date,symbol\n" +
            ") withdraw\n" +
            "LEFT JOIN\n" +
            "(\n" +
            "\tSELECT SUM(token_amount) recharge_amount, created_date recharge_created_date,symbol recharge_symbol,'Recharge' as recharge_operation \n" +
            "\t\tFROM top_transaction tt where `type` LIKE '%Recharge%' AND symbol <> 'BTC' and status='0x1' GROUP BY created_date,symbol\n" +
            "\tUNION \n" +
            "\tSELECT SUM(token_amount) recharge_amount, created_date recharge_created_date,symbol recharge_symbol,'Recharge' as recharge_operation \n" +
            "\t\tFROM top_transaction tt where `type` LIKE '%Recharge%' AND symbol = 'BTC' GROUP BY created_date,symbol\n" +
            ") recharge\n" +
            "on withdraw.withdraw_created_date = recharge.recharge_created_date and withdraw.withdraw_symbol = recharge.recharge_symbol\n" +
            "UNION \n" +
            "SELECT withdraw_amount, withdraw_created_date,withdraw_symbol,withdraw_operation,recharge_amount, recharge_created_date,recharge_symbol,recharge_operation from\n" +
            "(\n" +
            "\tSELECT SUM(token_amount) withdraw_amount, created_date withdraw_created_date,symbol withdraw_symbol,'Withdraw' as withdraw_operation  \n" +
            "\t\tFROM top_transaction tt where `type` LIKE '%Withdraw%' AND symbol <> 'BTC' and status='0x1' GROUP BY created_date,symbol\n" +
            "\tUNION \n" +
            "\tSELECT SUM(token_amount) withdraw_amount, created_date withdraw_created_date,symbol withdraw_symbol,'Withdraw' as withdraw_operation  \n" +
            "\t\tFROM top_transaction tt where `type` LIKE '%Withdraw%' AND symbol = 'BTC' GROUP BY created_date,symbol\n" +
            ") withdraw\n" +
            "RIGHT JOIN\n" +
            "(\n" +
            "\tSELECT SUM(token_amount) recharge_amount, created_date recharge_created_date,symbol recharge_symbol,'Recharge' as recharge_operation \n" +
            "\t\tFROM top_transaction tt where `type` LIKE '%Recharge%' AND symbol <> 'BTC' and status='0x1' GROUP BY created_date,symbol\n" +
            "\tUNION \n" +
            "\tSELECT SUM(token_amount) recharge_amount, created_date recharge_created_date,symbol recharge_symbol,'Recharge' as recharge_operation \n" +
            "\t\tFROM top_transaction tt where `type` LIKE '%Recharge%' AND symbol = 'BTC' GROUP BY created_date,symbol\n" +
            ") recharge\n" +
            "on withdraw.withdraw_created_date = recharge.recharge_created_date and withdraw.withdraw_symbol = recharge.recharge_symbol\n" +
            ") st \n" +
            ") st1 ORDER BY st1.operate_date")
    List<StatisticTransactionVO> getDayStatic(TransactionStatisticDTO transactionDayStatisticDTO);

    @Select("SELECT * from (\n" +
            "SELECT IFNULL(withdraw_amount,0)withdraw_amount,IFNULL(recharge_amount,0)recharge_amount,symbol,IFNULL(withdraw_created_date,recharge_created_date) operate_date FROM (\n" +
            "SELECT withdraw_amount,recharge_amount,CONCAT(withdraw_year,'-',LPAD(withdraw_month, 2, '0')) withdraw_created_date,IFNULL(withdraw_symbol,recharge_symbol) symbol,CONCAT(recharge_year,'-',LPAD(recharge_month, 2, '0')) recharge_created_date FROM\n" +
            "(\n" +
            "SELECT * \n" +
            "FROM \n" +
            "(\n" +
            "\tSELECT SUM(token_amount) withdraw_amount,YEAR(created_date) withdraw_year, MONTH(created_date) withdraw_month,symbol withdraw_symbol,'Withdraw' withdraw_operation  \n" +
            "\t\tFROM top_transaction tt where `type` LIKE '%Withdraw%' AND symbol <> 'BTC' and status='0x1' GROUP BY YEAR(created_date), MONTH(created_date),symbol\n" +
            "\tUNION\n" +
            "\tSELECT SUM(token_amount) withdraw_amount,YEAR(created_date) withdraw_year, MONTH(created_date) withdraw_month,symbol withdraw_symbol,'Withdraw' withdraw_operation  \n" +
            "\t\tFROM top_transaction tt where `type` LIKE '%Withdraw%' AND symbol = 'BTC' GROUP BY YEAR(created_date), MONTH(created_date),symbol\n" +
            ") withdraw\n" +
            "LEFT JOIN\n" +
            "(\n" +
            "\tSELECT SUM(token_amount) recharge_amount,YEAR(created_date) recharge_year, MONTH(created_date) recharge_month,symbol recharge_symbol,'Recharge' recharge_operation  \n" +
            "\t\tFROM top_transaction tt where `type` LIKE '%Recharge%' AND symbol <> 'BTC' and status='0x1'  GROUP BY YEAR(created_date), MONTH(created_date),symbol\n" +
            "\tUNION \n" +
            "\tSELECT SUM(token_amount) recharge_amount,YEAR(created_date) recharge_year, MONTH(created_date) recharge_month,symbol recharge_symbol,'Recharge' recharge_operation  \n" +
            "\t\tFROM top_transaction tt where `type` LIKE '%Recharge%' AND symbol = 'BTC'  GROUP BY YEAR(created_date), MONTH(created_date),symbol\n" +
            ") recharge\n" +
            "on withdraw.withdraw_year = recharge.recharge_year and withdraw.withdraw_month = recharge.recharge_month and withdraw.withdraw_symbol = recharge.recharge_symbol\n" +
            "UNION \n" +
            "SELECT * \n" +
            "FROM \n" +
            "(\n" +
            "\tSELECT SUM(token_amount) withdraw_amount,YEAR(created_date) withdraw_year, MONTH(created_date) withdraw_month,symbol withdraw_symbol,'Withdraw' withdraw_operation  \n" +
            "\t\tFROM top_transaction tt where `type` LIKE '%Withdraw%' AND symbol <> 'BTC' and status='0x1' GROUP BY YEAR(created_date), MONTH(created_date),symbol\n" +
            "\tUNION \n" +
            "\tSELECT SUM(token_amount) withdraw_amount,YEAR(created_date) withdraw_year, MONTH(created_date) withdraw_month,symbol withdraw_symbol,'Withdraw' withdraw_operation  \n" +
            "\t\tFROM top_transaction tt where `type` LIKE '%Withdraw%' AND symbol = 'BTC' GROUP BY YEAR(created_date), MONTH(created_date),symbol\n" +
            ") withdraw\n" +
            "RIGHT JOIN\n" +
            "(\n" +
            "\tSELECT SUM(token_amount) recharge_amount,YEAR(created_date) recharge_year, MONTH(created_date) recharge_month,symbol recharge_symbol,'Recharge' recharge_operation  \n" +
            "\t\tFROM top_transaction tt where `type` LIKE '%Recharge%' AND symbol <> 'BTC' and status='0x1'  GROUP BY YEAR(created_date), MONTH(created_date),symbol\n" +
            "\tUNION \n" +
            "\tSELECT SUM(token_amount) recharge_amount,YEAR(created_date) recharge_year, MONTH(created_date) recharge_month,symbol recharge_symbol,'Recharge' recharge_operation  \n" +
            "\t\tFROM top_transaction tt where `type` LIKE '%Recharge%' AND symbol = 'BTC'  GROUP BY YEAR(created_date), MONTH(created_date),symbol\n" +
            ") recharge\n" +
            "on withdraw.withdraw_year = recharge.recharge_year and withdraw.withdraw_month = recharge.recharge_month and withdraw.withdraw_symbol = recharge.recharge_symbol\n" +
            ") a\n" +
            ") a1 \n" +
            ") a2 ORDER by a2.operate_date")
    List<StatisticTransactionVO> getMonthStatic(TransactionStatisticDTO transactionDayStatisticDTO);
}
