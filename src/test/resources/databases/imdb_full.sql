-- MySQL dump 10.13  Distrib 5.7.24, for Linux (x86_64)
--
-- Host: localhost    Database: imdb_full
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
-- Table structure for table `actors`
--

DROP TABLE IF EXISTS `actors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actors` (
  `actorid` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(250) NOT NULL,
  `sex` enum('F','M') DEFAULT NULL,
  PRIMARY KEY (`actorid`),
  KEY `name` (`name`(10)),
  FULLTEXT KEY `actors_name_fulltext` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2749387 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `business`
--

DROP TABLE IF EXISTS `business`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business` (
  `movieid` mediumint(8) unsigned NOT NULL,
  `businesstext` mediumtext,
  PRIMARY KEY (`movieid`),
  CONSTRAINT `business_ibfk_1` FOREIGN KEY (`movieid`) REFERENCES `movies` (`movieid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `countries`
--

DROP TABLE IF EXISTS `countries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `countries` (
  `movieid` mediumint(8) unsigned NOT NULL,
  `country` varchar(50) NOT NULL,
  PRIMARY KEY (`movieid`,`country`),
  CONSTRAINT `countries_ibfk_1` FOREIGN KEY (`movieid`) REFERENCES `movies` (`movieid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `directors`
--

DROP TABLE IF EXISTS `directors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `directors` (
  `directorid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `name` varchar(250) NOT NULL,
  `rate` double(18,1) DEFAULT NULL,
  `gross` decimal(32,0) DEFAULT NULL,
  `num` bigint(21) NOT NULL DEFAULT '0',
  PRIMARY KEY (`directorid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `distributors`
--

DROP TABLE IF EXISTS `distributors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `distributors` (
  `movieid` mediumint(8) unsigned NOT NULL,
  `name` varchar(250) NOT NULL,
  PRIMARY KEY (`movieid`,`name`),
  CONSTRAINT `distributors_ibfk_1` FOREIGN KEY (`movieid`) REFERENCES `movies` (`movieid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `editors`
--

DROP TABLE IF EXISTS `editors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `editors` (
  `editorid` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(250) NOT NULL,
  PRIMARY KEY (`editorid`),
  KEY `name` (`name`(10))
) ENGINE=InnoDB AUTO_INCREMENT=202790 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `genres`
--

DROP TABLE IF EXISTS `genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `genres` (
  `movieid` mediumint(8) unsigned NOT NULL,
  `genre` varchar(50) NOT NULL,
  PRIMARY KEY (`movieid`,`genre`),
  KEY `movieid` (`movieid`),
  KEY `genre` (`genre`(15)),
  CONSTRAINT `genres_ibfk_1` FOREIGN KEY (`movieid`) REFERENCES `movies` (`movieid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `language`
--

DROP TABLE IF EXISTS `language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `language` (
  `movieid` mediumint(8) unsigned NOT NULL,
  `language` varchar(100) NOT NULL,
  PRIMARY KEY (`movieid`,`language`),
  KEY `index1` (`movieid`),
  CONSTRAINT `language_ibfk_1` FOREIGN KEY (`movieid`) REFERENCES `movies` (`movieid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `movies`
--

DROP TABLE IF EXISTS `movies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movies` (
  `movieid` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(400) NOT NULL,
  `year` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`movieid`),
  KEY `title` (`title`(15)),
  FULLTEXT KEY `movies_title_fulltext` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=2593314 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `movies2actors`
--

DROP TABLE IF EXISTS `movies2actors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movies2actors` (
  `movieid` mediumint(8) unsigned NOT NULL,
  `actorid` mediumint(8) unsigned NOT NULL,
  `as_character` varchar(1000) DEFAULT NULL,
  `leading` int(11) DEFAULT NULL,
  PRIMARY KEY (`movieid`,`actorid`),
  KEY `a` (`actorid`),
  KEY `movieid` (`movieid`),
  CONSTRAINT `movies2actors_ibfk_1` FOREIGN KEY (`movieid`) REFERENCES `movies` (`movieid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `movies2actors_ibfk_2` FOREIGN KEY (`actorid`) REFERENCES `actors` (`actorid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `movies2directors`
--

DROP TABLE IF EXISTS `movies2directors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movies2directors` (
  `movieid` mediumint(8) unsigned NOT NULL,
  `directorid` mediumint(8) unsigned NOT NULL,
  `genre` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`movieid`,`directorid`),
  KEY `movieid` (`movieid`),
  KEY `directorid` (`directorid`),
  CONSTRAINT `movies2directors_ibfk_1` FOREIGN KEY (`movieid`) REFERENCES `movies` (`movieid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `movies2directors_ibfk_2` FOREIGN KEY (`directorid`) REFERENCES `directors` (`directorid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `movies2editors`
--

DROP TABLE IF EXISTS `movies2editors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movies2editors` (
  `movieid` mediumint(8) unsigned NOT NULL,
  `editorid` mediumint(8) unsigned NOT NULL,
  `addition` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`movieid`,`editorid`),
  KEY `movieid` (`movieid`),
  KEY `editorid` (`editorid`),
  CONSTRAINT `movies2editors_ibfk_1` FOREIGN KEY (`movieid`) REFERENCES `movies` (`movieid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `movies2editors_ibfk_2` FOREIGN KEY (`editorid`) REFERENCES `editors` (`editorid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `movies2producers`
--

DROP TABLE IF EXISTS `movies2producers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movies2producers` (
  `movieid` mediumint(8) unsigned NOT NULL,
  `producerid` mediumint(8) unsigned NOT NULL,
  `addition` varchar(100) NOT NULL,
  PRIMARY KEY (`movieid`,`producerid`,`addition`),
  KEY `movieid` (`movieid`),
  KEY `producerid` (`producerid`),
  CONSTRAINT `movies2producers_ibfk_1` FOREIGN KEY (`movieid`) REFERENCES `movies` (`movieid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `movies2producers_ibfk_2` FOREIGN KEY (`producerid`) REFERENCES `producers` (`producerid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `movies2writers`
--

DROP TABLE IF EXISTS `movies2writers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movies2writers` (
  `movieid` mediumint(8) unsigned NOT NULL,
  `writerid` mediumint(8) unsigned NOT NULL,
  `addition` varchar(250) NOT NULL,
  PRIMARY KEY (`movieid`,`writerid`,`addition`),
  KEY `movieid` (`movieid`),
  KEY `writerid` (`writerid`),
  CONSTRAINT `movies2writers_ibfk_1` FOREIGN KEY (`movieid`) REFERENCES `movies` (`movieid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `movies2writers_ibfk_2` FOREIGN KEY (`writerid`) REFERENCES `writers` (`writerid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `prodcompanies`
--

DROP TABLE IF EXISTS `prodcompanies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prodcompanies` (
  `movieid` mediumint(8) unsigned NOT NULL,
  `name` varchar(250) NOT NULL,
  PRIMARY KEY (`movieid`,`name`),
  KEY `movieid` (`movieid`),
  KEY `name` (`name`(15)),
  CONSTRAINT `prodcompanies_ibfk_1` FOREIGN KEY (`movieid`) REFERENCES `movies` (`movieid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `producers`
--

DROP TABLE IF EXISTS `producers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producers` (
  `producerid` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(250) NOT NULL,
  PRIMARY KEY (`producerid`),
  KEY `name` (`name`(10)),
  FULLTEXT KEY `producers_name_fulltext` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=532348 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ratings`
--

DROP TABLE IF EXISTS `ratings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ratings` (
  `movieid` mediumint(8) unsigned NOT NULL,
  `rank` char(4) NOT NULL,
  `votes` mediumint(8) unsigned DEFAULT NULL,
  `distribution` char(10) NOT NULL,
  PRIMARY KEY (`movieid`),
  CONSTRAINT `ratings_ibfk_1` FOREIGN KEY (`movieid`) REFERENCES `movies` (`movieid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `runningtimes`
--

DROP TABLE IF EXISTS `runningtimes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `runningtimes` (
  `movieid` mediumint(8) unsigned NOT NULL,
  `time` varchar(50) NOT NULL,
  `addition` varchar(100) NOT NULL,
  `time1` int(11) DEFAULT NULL,
  PRIMARY KEY (`movieid`,`time`,`addition`),
  KEY `movieid` (`movieid`),
  CONSTRAINT `runningtimes_ibfk_1` FOREIGN KEY (`movieid`) REFERENCES `movies` (`movieid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `writers`
--

DROP TABLE IF EXISTS `writers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `writers` (
  `writerid` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(250) NOT NULL,
  PRIMARY KEY (`writerid`),
  KEY `name` (`name`(10))
) ENGINE=InnoDB AUTO_INCREMENT=407171 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-01  8:24:18
