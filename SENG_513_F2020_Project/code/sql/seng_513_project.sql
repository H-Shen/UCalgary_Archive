/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 80020
Source Host           : localhost:3306
Source Database       : seng_513_project

Target Server Type    : MYSQL
Target Server Version : 80020
File Encoding         : 65001

Date: 2020-12-20 19:45:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `uuid` varchar(36) NOT NULL,
  `username` varchar(256) NOT NULL,
  `password` varchar(256) NOT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of admin
-- ----------------------------

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `projectID` varchar(255) DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `rotate` varchar(255) DEFAULT NULL,
  `turn` varchar(5) CHARACTER SET armscii8 COLLATE armscii8_general_ci DEFAULT '0' COMMENT '0为不翻 1为翻',
  `opacity` varchar(255) DEFAULT NULL,
  `light` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of project
-- ----------------------------
INSERT INTO `project` VALUES ('1608457020605', '1608457020612.jpg', '', '', '', '');
INSERT INTO `project` VALUES ('1608460194360', '1608460194370.jpg', '', '', '', '');
INSERT INTO `project` VALUES ('1608460213988', '1608460213998.jpg', '', '', '', '');

-- ----------------------------
-- Table structure for team
-- ----------------------------
DROP TABLE IF EXISTS `team`;
CREATE TABLE `team` (
  `teamID` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `member` varchar(255) DEFAULT NULL,
  `project` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of team
-- ----------------------------
INSERT INTO `team` VALUES ('1608445689127', '1608445689127', 'abc', '1608445693712');
INSERT INTO `team` VALUES ('1608446041727', '1608446041727', 'abc,123', '1608446050547');
INSERT INTO `team` VALUES ('1608448470054', '1608448470054', '123,abc', '1608448477663');
INSERT INTO `team` VALUES ('1608449359742', '1608449359742', 'abc', '1608449370906,1608460213988');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `username` varchar(256) NOT NULL,
  `email` varchar(256) NOT NULL,
  `password` varchar(256) NOT NULL,
  `password_question` varchar(256) NOT NULL,
  `answer_of_password_question` varchar(256) NOT NULL,
  `project` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('user0', 'samuellee@test.com', '123', 'question0', 'answer0', '');
INSERT INTO `user` VALUES ('user1', 'jamesoconnell@test.com', '123123', 'question1', 'answer1', '');
INSERT INTO `user` VALUES ('user2', 'nickpit12314@test.com', 'password2', 'question2', 'answer2', null);
INSERT INTO `user` VALUES ('SamuelL2', 'samuellee@test.com', 'password0', 'question0', 'answer0', null);
INSERT INTO `user` VALUES ('JamesOC2', 'jamesoconnell@test.com', '123123', 'question0', 'answer1', null);
INSERT INTO `user` VALUES ('NickPit2', 'nickpit12314@test.com', 'password2', 'question0', 'answer2', null);
INSERT INTO `user` VALUES ('123', '123', '123', 'What primary school did you attend?', '123', '1608440248435,1608443845556,1608443994775,1608460194360');
INSERT INTO `user` VALUES ('abc', 'abc', 'abc', 'What primary school did you attend?', 'abc', null);
