/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : localhost:3306
 Source Schema         : smilex-pd

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 27/01/2022 16:15:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pd_log
-- ----------------------------
DROP TABLE IF EXISTS `pd_log`;
CREATE TABLE `pd_log`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `log_type` tinyint(2) NULL DEFAULT NULL COMMENT '操作类型',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pd_log_bin
-- ----------------------------
DROP TABLE IF EXISTS `pd_log_bin`;
CREATE TABLE `pd_log_bin`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `log_id` bigint(20) NOT NULL COMMENT '日志->logId',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作内容记录',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志的执行记录\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pd_log_undo
-- ----------------------------
DROP TABLE IF EXISTS `pd_log_undo`;
CREATE TABLE `pd_log_undo`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `log_id` bigint(20) NOT NULL COMMENT '日志id',
  `log_bin_id` bigint(20) NOT NULL COMMENT '日志执行记录id',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '回滚日志内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志的回滚记录' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
