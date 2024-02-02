

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_store
-- ----------------------------
DROP TABLE IF EXISTS `top_store`;
CREATE TABLE `top_store`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `meb_id` bigint NOT NULL COMMENT '用户ID',
  `store_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '存单号',
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '存入币种',
  `amount` decimal(40, 20) NOT NULL COMMENT '存入金额',
  `store_date` datetime NOT NULL COMMENT '存入时间',
  `release_date` datetime NOT NULL COMMENT '释放时间',
  `created_date` datetime NOT NULL COMMENT '创建日期',
  `created_by` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `updated_by` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_store_no`(`store_no` ASC) USING BTREE,
  INDEX `index_meb_id`(`meb_id` ASC) USING BTREE,
  INDEX `index_store_date`(`store_date` ASC) USING BTREE,
  INDEX `index_release_date`(`release_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
