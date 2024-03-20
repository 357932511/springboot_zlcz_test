/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : localhost:3306
 Source Schema         : zlcz_test

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 20/03/2024 12:43:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for inventory_ledger
-- ----------------------------
DROP TABLE IF EXISTS `inventory_ledger`;
CREATE TABLE `inventory_ledger`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '台账ID',
  `product_id` int NOT NULL COMMENT '商品ID',
  `store_id` int NOT NULL COMMENT '门店ID',
  `quantity` int NOT NULL COMMENT '数量',
  `type` int NOT NULL COMMENT '操作类型 (0: 出库, 1: 入库)',
  `ledger_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `is_deleted` tinyint NULL DEFAULT NULL COMMENT '是否删除',
  `product_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '商品进出库台账' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of inventory_ledger
-- ----------------------------
INSERT INTO `inventory_ledger` VALUES (1, 2, 1, 100, 1, '2024-03-20 12:32:56', 0, '商品4');
INSERT INTO `inventory_ledger` VALUES (2, 2, 1, 10, 0, '2024-03-20 12:33:33', 0, '商品4');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `outbound_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '出库时间',
  `message_body` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '消息正文',
  `is_read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已阅读 (0: 未阅读, 1: 已阅读)',
  `push_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '推送时间',
  `ledger_id` int NOT NULL COMMENT '关联的台账ID',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '消息表，用于广播消息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (1, '2024-03-20 12:33:33', '门店xxx出库了10个商品4', 0, '2024-03-20 12:33:33', 2, 1);

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '库存id',
  `product_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_spec` int NULL DEFAULT NULL COMMENT '商品规格数量',
  `product_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单个商品的价格',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '标识商品是否被删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '商品创建时间',
  `product_id` int NULL DEFAULT NULL COMMENT '商品id',
  `store_id` int NULL DEFAULT NULL COMMENT '门店id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (2, '商品名称', 10, 20.50, 1, '2024-03-20 17:00:00', 1001, 1);
INSERT INTO `product` VALUES (3, '商品3', 8, 25.75, 0, '2024-03-20 11:00:00', 1, 2);
INSERT INTO `product` VALUES (4, '商品4', 105, 15.99, 0, '2024-03-20 12:00:00', 2, 1);
INSERT INTO `product` VALUES (5, '商品5', 20, 12.00, 0, '2024-03-20 13:00:00', 1, 3);
INSERT INTO `product` VALUES (6, '商品6', 6, 18.25, 0, '2024-03-20 14:00:00', 1, 4);
INSERT INTO `product` VALUES (7, '商品7', 12, 22.80, 0, '2024-03-20 15:00:00', 1, 5);
INSERT INTO `product` VALUES (8, '商品8', 7, 35.50, 0, '2024-03-20 16:00:00', 1, 6);
INSERT INTO `product` VALUES (9, '商品9', 18, 10.99, 0, '2024-03-20 17:00:00', 3, 1);
INSERT INTO `product` VALUES (10, '商品10', 9, 28.00, 0, '2024-03-20 18:00:00', 3, 2);
INSERT INTO `product` VALUES (11, '商品11', 11, 21.35, 0, '2024-03-20 19:00:00', 2, 2);
INSERT INTO `product` VALUES (12, '商品12', 16, 16.50, 0, '2024-03-20 20:00:00', 1, 7);
INSERT INTO `product` VALUES (13, '商品13', 4, 40.25, 0, '2024-03-20 21:00:00', 3, 3);
INSERT INTO `product` VALUES (14, '商品14', 14, 24.99, 0, '2024-03-20 22:00:00', 4, 1);
INSERT INTO `product` VALUES (15, '商品15', 17, 13.75, 0, '2024-03-20 23:00:00', 5, 1);
INSERT INTO `product` VALUES (16, '商品16', 3, 50.00, 0, '2024-03-20 00:00:00', 2, 3);
INSERT INTO `product` VALUES (17, '商品17', 13, 27.80, 0, '2024-03-20 01:00:00', 1, 8);
INSERT INTO `product` VALUES (18, '商品18', 19, 11.25, 0, '2024-03-20 02:00:00', 2, 4);
INSERT INTO `product` VALUES (19, '商品19', 2, 45.99, 0, '2024-03-20 03:00:00', 3, 4);
INSERT INTO `product` VALUES (20, '商品20', 10, 31.50, 0, '2024-03-20 04:00:00', 2, 5);

SET FOREIGN_KEY_CHECKS = 1;
