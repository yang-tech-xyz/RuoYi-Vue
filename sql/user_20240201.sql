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
