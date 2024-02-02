

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_account_tx
-- ----------------------------
DROP TABLE IF EXISTS `t_account_tx`;
CREATE TABLE `t_account_tx`  (
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
  INDEX `index_symbol`(`symbol` ASC) USING BTREE,
  INDEX `index_type`(`tx_type` ASC) USING BTREE,
  INDEX `index_date`(`created_date` ASC) USING BTREE,
  INDEX `index_balance_type`(`balance_type` ASC) USING BTREE,
  INDEX `index_ref_no`(`ref_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35437 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
