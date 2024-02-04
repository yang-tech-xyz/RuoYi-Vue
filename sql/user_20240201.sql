CREATE TABLE `ry-vue`.top_user (
	id INT auto_increment primary key NOT NULL COMMENT '自增id',
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

CREATE TABLE `ry-vue`.top_transacttion (
	id bigint auto_increment primary key NOT NULL COMMENT 'id',
	hash varchar(100) NULL COMMENT '事务hash值',
	chain_id int NULL COMMENT '链id',
	token_id INT NULL COMMENT '链id',
	rpc_endpoint varchar(100) NULL COMMENT '链RPC地址'
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci
COMMENT='用户充值提现信息表';

ALTER TABLE `ry-vue`.top_transacttion ADD status varchar(100) NULL COMMENT '事务状态';
ALTER TABLE `ry-vue`.top_transacttion ADD symbol varchar(100) NULL COMMENT 'token标记';
ALTER TABLE `ry-vue`.top_transacttion ADD amount decimal(20,10) NULL COMMENT '充值数量';
CREATE UNIQUE INDEX top_transaction_hash_IDX USING BTREE ON `ry-vue`.top_transaction (hash);
ALTER TABLE `ry-vue`.top_transaction ADD comfirm tinyint NULL COMMENT '确认是否充值成功,0，充值成功，1：充值失败';
ALTER TABLE `ry-vue`.top_transaction ADD height BIGINT NULL COMMENT '事务区块高度';



