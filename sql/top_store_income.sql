

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_store_income
-- ----------------------------
DROP TABLE IF EXISTS `top_store_income`;
CREATE TABLE `top_store_income`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `meb_id` bigint NOT NULL COMMENT '用户ID',
  `store_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '存单号',
  `token_price` decimal(40, 20) NOT NULL DEFAULT 0.00000000000000000000 COMMENT '存币U价',
  `income` decimal(40, 20) NOT NULL DEFAULT 0.00000000000000000000 COMMENT '收益金额',
  `income_rate` decimal(40, 20) NOT NULL DEFAULT 0.00000000000000000000 COMMENT '收益比例',
  `income_date` date NOT NULL COMMENT '收益时间',
  `created_date` datetime NOT NULL COMMENT '创建日期',
  `created_by` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `updated_by` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_meb_id`(`meb_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_store_no_date`(`store_no` ASC, `income_date` ASC) USING BTREE,
  INDEX `index_income_date`(`income_date` ASC) USING BTREE,
  INDEX `index_store_no`(`store_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
