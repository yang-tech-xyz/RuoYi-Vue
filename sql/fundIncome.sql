ALTER TABLE `ry-vue`.top_transaction ADD created_date DATE NULL;
ALTER TABLE `ry-vue`.top_transaction ADD updated_date DATE NULL;

UPDATE top_transaction set created_date = CAST(create_time as DATE);

UPDATE top_transaction set updated_date  = CAST(update_time  as DATE);


SELECT withdraw_amount, withdraw_created_date,withdraw_symbol,withdraw_operation,recharge_amount, recharge_created_date,recharge_symbol,recharge_operation from
(SELECT SUM(token_amount) withdraw_amount, created_date withdraw_created_date,symbol withdraw_symbol,'Withdraw' as withdraw_operation  FROM top_transaction tt where `type` LIKE '%Withdraw%' and status='0x1' GROUP BY created_date,symbol) withdraw
RIGHT JOIN
(SELECT SUM(token_amount) recharge_amount, created_date recharge_created_date,symbol recharge_symbol,'Recharge' as recharge_operation FROM top_transaction tt where `type` LIKE '%Recharge%' and status='0x1' GROUP BY created_date,symbol) recharge
on withdraw.withdraw_created_date = recharge.recharge_created_date and withdraw.withdraw_symbol = recharge.recharge_symbol


-- 当月统计

SELECT withdraw_amount,CONCAT(withdraw_year,'-',withdraw_month) withdraw_created_date,withdraw_symbol,withdraw_operation,recharge_amount,CONCAT(recharge_year,'-',recharge_month) recharge_created_date,recharge_symbol,recharge_operation FROM
(SELECT * FROM (SELECT SUM(token_amount) withdraw_amount,YEAR(created_date) withdraw_year, MONTH(created_date) withdraw_month,symbol withdraw_symbol,'Withdraw' withdraw_operation  FROM top_transaction tt where `type` LIKE '%Withdraw%' and status='0x1' GROUP BY YEAR(created_date), MONTH(created_date),symbol) withdraw
RIGHT JOIN
(SELECT SUM(token_amount) recharge_amount,YEAR(created_date) recharge_year, MONTH(created_date) recharge_month,symbol recharge_symbol,'Recharge' recharge_operation  FROM top_transaction tt where `type` LIKE '%Recharge%' and status='0x1'  GROUP BY YEAR(created_date), MONTH(created_date),symbol) recharge
on withdraw.withdraw_year = recharge.recharge_year and withdraw.withdraw_month = recharge.recharge_month and withdraw.withdraw_symbol = recharge.recharge_symbol) a
