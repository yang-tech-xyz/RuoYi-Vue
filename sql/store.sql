SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for top_account
-- ----------------------------
DROP TABLE IF EXISTS `top_account`;
CREATE TABLE `top_account`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `meb_id` bigint NOT NULL COMMENT '会员ID',
  `symbol` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代币',
  `available_balance` decimal(40, 20) NOT NULL COMMENT '可用金额',
  `lockup_balance` decimal(40, 20) NOT NULL COMMENT '锁仓金额',
  `frozen_balance` decimal(40, 20) NOT NULL COMMENT '冻结金额',
  `created_date` datetime NOT NULL COMMENT '创建日期',
  `created_by` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `updated_by` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_meb_id`(`meb_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_mebId_token`(`meb_id` ASC, `symbol` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for top_account_tx
-- ----------------------------
DROP TABLE IF EXISTS `top_account_tx`;
CREATE TABLE `top_account_tx`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `meb_id` bigint NOT NULL COMMENT '会员ID',
  `account_id` bigint NOT NULL COMMENT '资产ID',
  `symbol` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代币',
  `amount` decimal(40, 20) NOT NULL COMMENT '交易金额',
  `fee` decimal(40, 20) NOT NULL COMMENT '手续费',
  `balance_before` decimal(40, 20) NOT NULL COMMENT '变动之前',
  `balance_after` decimal(40, 20) NOT NULL COMMENT '变动之后',
  `transaction_no` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流水号',
  `balance_type` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资产类型(available = 可用, frozen = 冻结,lockup = 限制)',
  `tx_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流水类型',
  `ref_no` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方号',
  `unique_id` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一号，用于幂等',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `created_date` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `created_by` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `updated_by` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_unique_id`(`unique_id` ASC) USING BTREE,
  INDEX `index_meb_id`(`meb_id` ASC) USING BTREE,
  INDEX `index_type`(`tx_type` ASC) USING BTREE,
  INDEX `index_date`(`created_date` ASC) USING BTREE,
  INDEX `index_balance_type`(`balance_type` ASC) USING BTREE,
  INDEX `index_ref_no`(`ref_no` ASC) USING BTREE,
  INDEX `index_token`(`symbol` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for top_store
-- ----------------------------
DROP TABLE IF EXISTS `top_store`;
CREATE TABLE `top_store`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品名称',
  `period` int NOT NULL COMMENT '产品周期（月）,每月限定30天',
  `symbol` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '存入币种',
  `income_symbol` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收益币种',
  `min_order_amount` decimal(40, 20) NOT NULL COMMENT '最小投资额',
  `max_order_amount` decimal(40, 20) NOT NULL COMMENT '最大投注额',
  `rate` decimal(40, 20) NOT NULL COMMENT '收益利率',
  `status` int NOT NULL COMMENT '状态：1=有效，2=无效',
  `created_date` datetime NOT NULL COMMENT '创建日期',
  `created_by` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `updated_by` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_status`(`status` ASC) USING BTREE,
  INDEX `index_token`(`symbol` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for top_store_order
-- ----------------------------
DROP TABLE IF EXISTS `top_store_order`;
CREATE TABLE `top_store_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `store_id` bigint NOT NULL COMMENT '产品ID',
  `meb_id` bigint NOT NULL COMMENT '用户ID',
  `order_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '存单号',
  `symbol` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '存入币种',
  `price` decimal(40, 20) NOT NULL COMMENT '币种价格',
  `amount` decimal(40, 20) NOT NULL COMMENT '存入金额',
  `rate` decimal(40, 20) NOT NULL COMMENT '利率',
  `income_symbol` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收益币种',
  `income` decimal(40, 20) NOT NULL COMMENT '收益',
  `store_date` datetime NOT NULL COMMENT '存入时间',
  `release_date` date NOT NULL COMMENT '释放时间',
  `redeem_date` datetime NULL DEFAULT NULL COMMENT '领取时间',
  `status` int NOT NULL COMMENT '状态：1=收益中，2=已领取',
  `created_date` datetime NOT NULL COMMENT '创建日期',
  `created_by` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `updated_by` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no` ASC) USING BTREE,
  INDEX `index_meb_id`(`meb_id` ASC) USING BTREE,
  INDEX `index_store_date`(`store_date` ASC) USING BTREE,
  INDEX `index_release_date`(`release_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for top_token_price
-- ----------------------------
DROP TABLE IF EXISTS `top_token_price`;
CREATE TABLE `top_token_price`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `symbol` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '币种',
  `price` decimal(40, 20) NOT NULL COMMENT '价格',
  `created_date` datetime NOT NULL COMMENT '创建日期',
  `created_by` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `updated_by` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_token`(`symbol` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
