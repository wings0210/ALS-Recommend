/*
 Navicat Premium Data Transfer

 Source Server         : text
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : food_rec

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 02/06/2022 09:47:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '123456');
INSERT INTO `user` VALUES ('10', '123456');
INSERT INTO `user` VALUES ('11', '123456');
INSERT INTO `user` VALUES ('12', '123456');
INSERT INTO `user` VALUES ('13', '123456');
INSERT INTO `user` VALUES ('14', '123456');
INSERT INTO `user` VALUES ('15', '123456');
INSERT INTO `user` VALUES ('16', '123456');
INSERT INTO `user` VALUES ('17', '123456');
INSERT INTO `user` VALUES ('18', '123456');
INSERT INTO `user` VALUES ('19', '123456');
INSERT INTO `user` VALUES ('2', '123456');
INSERT INTO `user` VALUES ('20', '123456');
INSERT INTO `user` VALUES ('3', '123456');
INSERT INTO `user` VALUES ('4', '123456');
INSERT INTO `user` VALUES ('5', '123456');
INSERT INTO `user` VALUES ('6', '123456');
INSERT INTO `user` VALUES ('7', '123456');
INSERT INTO `user` VALUES ('8', '123456');
INSERT INTO `user` VALUES ('9', '123456');

SET FOREIGN_KEY_CHECKS = 1;
