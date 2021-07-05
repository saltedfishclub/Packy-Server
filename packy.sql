-- MySQL dump 10.13  Distrib 5.7.33, for Win64 (x86_64)
--
-- Host: localhost    Database: Packy
-- ------------------------------------------------------
-- Server version	5.7.33-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `packy_packages`
--

DROP TABLE IF EXISTS `packy_packages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `packy_packages` (
  `pkg_id` int(11) NOT NULL AUTO_INCREMENT,
  `pkg_name` varchar(45) NOT NULL,
  `pkg_agreement` varchar(60) DEFAULT NULL,
  `pkg_authors` varchar(60) NOT NULL,
  `pkg_arch` varchar(45) DEFAULT NULL,
  `pkg_conflicts` varchar(60) DEFAULT NULL,
  `pkg_depends` varchar(60) DEFAULT NULL,
  `pkg_desc` varchar(100) NOT NULL,
  `pkg_java_version` varchar(45) DEFAULT NULL,
  `pkg_last_update` datetime NOT NULL,
  `pkg_mc_version` varchar(45) DEFAULT NULL,
  `pkg_version` varchar(45) NOT NULL,
  `pkg_verified` tinyint(4) NOT NULL,
  `pkg_icon` varchar(64) NOT NULL,
  PRIMARY KEY (`pkg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `packy_packages`
--

LOCK TABLES `packy_packages` WRITE;
/*!40000 ALTER TABLE `packy_packages` DISABLE KEYS */;
/*!40000 ALTER TABLE `packy_packages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `packy_resources`
--

DROP TABLE IF EXISTS `packy_resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `packy_resources` (
  `res_id` varchar(36) NOT NULL,
  `res_auth` varchar(45) DEFAULT NULL,
  `res_pkg` varchar(45) DEFAULT NULL,
  `res_version` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`res_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `packy_resources`
--

LOCK TABLES `packy_resources` WRITE;
/*!40000 ALTER TABLE `packy_resources` DISABLE KEYS */;
INSERT INTO `packy_resources` VALUES ('959f5ccb-7fd0-4354-b359-991de37599c1',NULL,NULL,NULL);
/*!40000 ALTER TABLE `packy_resources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `packy_users`
--

DROP TABLE IF EXISTS `packy_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `packy_users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) DEFAULT NULL,
  `user_join_time` datetime DEFAULT NULL,
  `user_email` varchar(45) DEFAULT NULL,
  `user_perm` varchar(45) DEFAULT NULL,
  `user_publish_pkgs` varchar(45) DEFAULT NULL,
  `user_bio` longtext,
  `user_pass` varchar(64) DEFAULT NULL,
  `user_captcha` varchar(45) DEFAULT NULL,
  `user_checked_email` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `packy_userscol_UNIQUE` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `packy_users`
--

LOCK TABLES `packy_users` WRITE;
/*!40000 ALTER TABLE `packy_users` DISABLE KEYS */;
INSERT INTO `packy_users` VALUES (4,'D','2021-05-02 07:02:11','ziyun.luo@outlook.com','NORMAL',NULL,NULL,'88d4266fd4e6338d13b845fcf289579d209c897823b9217da3e161936f031589','7dd0c0f2e5e14751a834ed124817187c',0);
/*!40000 ALTER TABLE `packy_users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-07-05 15:35:19
