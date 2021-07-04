-- MySQL dump 10.13  Distrib 5.7.24, for Linux (x86_64)
--
-- Host: localhost    Database: generator_test
-- ------------------------------------------------------
-- Server version	5.5.5-10.1.35-MariaDB

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
-- Table structure for table `all_available_types`
--

DROP TABLE IF EXISTS `all_available_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `all_available_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `varchar` varchar(50) NOT NULL,
  `decimal` decimal(10,2) DEFAULT NULL,
  `unsigned_decimal` decimal(5,2) unsigned DEFAULT NULL,
  `datetime` datetime DEFAULT NULL,
  `blob` blob,
  `binary` binary(1) DEFAULT NULL,
  `longblob` longblob,
  `date` date DEFAULT NULL,
  `time` time DEFAULT NULL,
  `year` year(4) DEFAULT NULL,
  `geometry` geometry DEFAULT NULL,
  `real` double DEFAULT NULL,
  `char` char(10) DEFAULT NULL,
  `nvarchar` varchar(2500) CHARACTER SET utf8 DEFAULT NULL,
  `medium_text` mediumtext,
  `boolean` tinyint(4) DEFAULT NULL,
  `enum` set('A','B','C') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`varchar`)
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `self_reference`
--

DROP TABLE IF EXISTS `self_reference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `self_reference` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `parent` int(11) DEFAULT NULL,
  `table_a_pk_col_one` int(11) NOT NULL,
  `table_a_pk_col_two` char(5) NOT NULL,
  `table_a_pk_col_three` date NOT NULL,
  PRIMARY KEY (`id`,`table_a_pk_col_one`,`table_a_pk_col_two`,`table_a_pk_col_three`),
  KEY `self_reference_parent_idx` (`parent`),
  KEY `fk_self_reference_table_a1_idx` (`table_a_pk_col_one`,`table_a_pk_col_two`,`table_a_pk_col_three`),
  CONSTRAINT `fk_self_reference_table_a1` FOREIGN KEY (`table_a_pk_col_one`, `table_a_pk_col_two`, `table_a_pk_col_three`) REFERENCES `table_a` (`pk_col_one`, `pk_col_two`, `pk_col_three`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `self_reference_parent` FOREIGN KEY (`parent`) REFERENCES `self_reference` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `table1`
--

DROP TABLE IF EXISTS `table1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `table1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `table_a`
--

DROP TABLE IF EXISTS `table_a`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `table_a` (
  `pk_col_one` int(11) NOT NULL,
  `pk_col_two` char(5) NOT NULL,
  `pk_col_three` date NOT NULL,
  `another_col` varchar(45) DEFAULT NULL,
  `and_another` varchar(45) DEFAULT NULL,
  `table_b_id` datetime NOT NULL,
  `table_c_id` int(11) NOT NULL,
  PRIMARY KEY (`pk_col_one`,`pk_col_two`,`pk_col_three`),
  KEY `fk_table_a_table_b1_idx` (`table_b_id`),
  KEY `fk_table_a_table_c1_idx` (`table_c_id`),
  CONSTRAINT `fk_table_a_table_b1` FOREIGN KEY (`table_b_id`) REFERENCES `table_b` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_table_a_table_c1` FOREIGN KEY (`table_c_id`) REFERENCES `table_c` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `table_a_has_table1`
--

DROP TABLE IF EXISTS `table_a_has_table1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `table_a_has_table1` (
  `table_a_pk_col_one` int(11) NOT NULL,
  `table_a_pk_col_two` char(5) NOT NULL,
  `table_a_pk_col_three` date NOT NULL,
  `table1_id` int(11) NOT NULL,
  PRIMARY KEY (`table_a_pk_col_one`,`table_a_pk_col_two`,`table_a_pk_col_three`,`table1_id`),
  KEY `fk_table_a_has_table1_table11_idx` (`table1_id`),
  KEY `fk_table_a_has_table1_table_a1_idx` (`table_a_pk_col_one`,`table_a_pk_col_two`,`table_a_pk_col_three`),
  CONSTRAINT `fk_table_a_has_table1_table11` FOREIGN KEY (`table1_id`) REFERENCES `table1` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_table_a_has_table1_table_a1` FOREIGN KEY (`table_a_pk_col_one`, `table_a_pk_col_two`, `table_a_pk_col_three`) REFERENCES `table_a` (`pk_col_one`, `pk_col_two`, `pk_col_three`) ON DELETE NO ACTION ON UPDATE NO ACTION
)  COMMENT='relation table multiple primary key at one side';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `table_b`
--

DROP TABLE IF EXISTS `table_b`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `table_b` (
  `id` datetime NOT NULL,
  `you` varchar(45) DEFAULT NULL,
  `he` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `table_c`
--

DROP TABLE IF EXISTS `table_c`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `table_c` (
  `id` int(11) NOT NULL,
  `title` varchar(666) DEFAULT NULL,
  `table1_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`table1_id`),
  KEY `fk_table_c_table11_idx` (`table1_id`),
  CONSTRAINT `fk_table_c_table11` FOREIGN KEY (`table1_id`) REFERENCES `table1` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-01  8:20:53
