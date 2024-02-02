

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_account
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
  UNIQUE INDEX `uk_mebId_symbol`(`meb_id` ASC, `symbol` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15434 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
