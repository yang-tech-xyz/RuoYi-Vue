CREATE TABLE `ry-vue`.top_user (
	id bigint auto_increment primary key NOT NULL COMMENT '自增id',
	wallet varchar(100) NOT NULL COMMENT '钱包地址',
	create_time DATETIME NOT NULL,
	invited_code varchar(100) NOT NULL COMMENT '邀请码',
	invited_user_code varchar(100) NOT NULL COMMENT '邀请人的邀请码',
	update_time DATETIME NOT NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

CREATE UNIQUE INDEX top_user_wallet_IDX USING BTREE ON `ry-vue`.top_user (wallet);
CREATE UNIQUE INDEX top_user_invited_code_IDX USING BTREE ON `ry-vue`.top_user (invited_code);

INSERT INTO `ry-vue`.top_user (wallet,create_time,invited_code,invited_user_code,update_time) VALUES
	 ('1','2024-02-02 12:00:00','123','111','2024-02-02 12:00:00');


CREATE TABLE `ry-vue`.top_token (
	id INT auto_increment primary key NOT NULL COMMENT '主键',
	symbol varchar(100) NOT NULL COMMENT 'token 名称',
	decimals INT NOT NULL COMMENT '小数位',
	create_time DATETIME NULL,
	update_time DATETIME NULL,
	create_by varchar(100) NULL,
	update_by varchar(100) NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci
COMMENT='币种配置表';


CREATE TABLE `ry-vue`.top_chain (
	id int auto_increment primary key NOT NULL COMMENT '链信息配置表',
	chain_type varchar(100) NULL COMMENT '链类型',
	rpc_endpoint varchar(100) NULL COMMENT 'rpc 节点url',
	block_confirm INT NULL COMMENT '在多少个区块确认之后 才确认充值成功',
	create_time DATETIME NULL,
	update_time DATETIME NULL,
	create_by varchar(100) NULL,
	update_by varchar(100) NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE `ry-vue`.top_token ADD online1 tinyint NULL COMMENT '是否上线：0，上线，1下线';

ALTER TABLE `ry-vue`.top_chain ADD chain_id int NULL COMMENT '链id';


CREATE TABLE `ry-vue`.top_token_chain (
	id INT auto_increment primary key NOT NULL COMMENT '主键',
	token_id int NOT NULL COMMENT 'tokenid',
	chain_id INT NOT NULL COMMENT '链id',
	erc20_address varchar(100) NOT NULL COMMENT '官方该币种在该链上的erc20合约地址',
	create_time DATETIME NULL,
	update_time DATETIME NULL,
	create_by varchar(100) NULL,
	update_by varchar(100) NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci
COMMENT='币种-链关系配置表';

ALTER TABLE `ry-vue`.top_chain ADD receive_addres varchar(100) NOT NULL COMMENT '项目方收款地址';

-- `ry-vue`.top_transaction definition

CREATE TABLE `top_transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `hash` varchar(100) DEFAULT NULL COMMENT '事务hash值',
  `chain_id` int DEFAULT NULL COMMENT '链id',
  `token_id` int DEFAULT NULL COMMENT '链id',
  `rpc_endpoint` varchar(100) DEFAULT NULL COMMENT '链RPC地址',
  `status` varchar(100) DEFAULT NULL COMMENT '事务状态',
  `symbol` varchar(100) DEFAULT NULL COMMENT 'token标记',
  `token_amount` decimal(20,10) DEFAULT NULL COMMENT '充值数量',
  `is_confirm` bigint DEFAULT NULL COMMENT '确认是否充值成功,0，充值成功，1：充值失败',
  `height` bigint DEFAULT NULL COMMENT '事务区块高度',
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `block_confirm` int DEFAULT NULL COMMENT '多少个高度后确认交易成功',
  PRIMARY KEY (`id`),
  UNIQUE KEY `top_transaction_hash_IDX` (`hash`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户充值提现信息表';


CREATE TABLE `ry-vue`.top_power_config (
	id int auto_increment primary key NOT NULL COMMENT '主键',
	price DECIMAL(20,10) NULL COMMENT '算力购买价格.默认1000U',
	period INT NULL COMMENT '产出周期,默认360天',
	output_symbol varchar(100) NULL COMMENT '产出币种,默认BTC',
	output_ratio decimal(20,10) NULL COMMENT '产出比例',
	create_time datetime NULL,
	update_time datetime NULL,
	create_by varchar(100) NULL,
	update_by varchar(100) NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci
COMMENT='算力配置表';

CREATE TABLE `ry-vue`.top_power_order (
	id bigint auto_increment NOT NULL COMMENT 'id',
	user_id bigint NOT NULL COMMENT '用户ID',
	order_no varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
	amount decimal(20,10) NOT NULL COMMENT '购买金额,价格乘以购买数量等于购买金额',
	`number` int NOT NULL COMMENT '购买台数',
	output_symbol varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '产出币种',
	period int NOT NULL COMMENT '产出周期,默认值360天',
	output_ratio decimal(20,10) NOT NULL COMMENT '产出率',
	expected_total_output decimal(20,10) NOT NULL COMMENT '预估总产出',
	end_time datetime NOT NULL COMMENT '退出日期',
	create_by varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
	create_time datetime NOT NULL,
	update_by varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
	update_time datetime NULL,
	CONSTRAINT `PRIMARY` PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci
COMMENT='算力订单表';

ALTER TABLE `ry-vue`.top_power_order ADD end_date datetime NULL COMMENT '退出日期';
ALTER TABLE `ry-vue`.top_transaction ADD `type` varchar(100) NULL COMMENT '事务类型,recharge充值,withdraw提现';


ALTER TABLE `ry-vue`.top_power_order ADD end_time datetime NULL COMMENT '退出日期';

INSERT INTO `ry-vue`.top_power_config
(id, price, period, output_symbol, output_ratio, create_time, update_time, create_by, update_by)
VALUES(1, 1000.0000000000, 360, 'BTC', 200.0000000000, '2024-02-02 00:00:00', '2024-02-02 00:00:00', 'sys', 'sys');

ALTER TABLE `ry-vue`.top_power_order ADD order_no varchar(100) NULL COMMENT '订单编号';
ALTER TABLE `ry-vue`.top_power_config ADD curve varchar(100) NULL COMMENT '不要问我这个的注释';
ALTER TABLE `ry-vue`.top_power_config MODIFY COLUMN curve text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '不要问我这个的注释';
ALTER TABLE `ry-vue`.top_user ADD invited_user_id bigint NULL COMMENT '邀请人用户id';


ALTER TABLE `ry-vue`.top_user ADD grade int NULL COMMENT '等级';

ALTER TABLE `ry-vue`.top_user MODIFY COLUMN grade int DEFAULT 1 NOT NULL COMMENT '等级';
ALTER TABLE `ry-vue`.top_user ADD btc_transfer_address varchar(100) NULL COMMENT 'btc转账地址';
