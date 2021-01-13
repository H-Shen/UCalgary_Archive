/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 80020
Source Host           : localhost:3306
Source Database       : seng_513_project

Target Server Type    : MYSQL
Target Server Version : 80020
File Encoding         : 65001

Date: 2020-12-18 15:09:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` int NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `rotate` int DEFAULT NULL,
  `turn` varchar(5) CHARACTER SET armscii8 COLLATE armscii8_general_ci DEFAULT '0' COMMENT '0为不翻 1为翻',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of project
-- ----------------------------
