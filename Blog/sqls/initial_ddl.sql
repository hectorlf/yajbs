-- MySQL dump 10.13  Distrib 5.5.30, for Linux (x86_64)
--
-- Host: localhost    Database: 
-- ------------------------------------------------------
-- Server version	5.5.30

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
-- Current Database: `blog_db`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `blog_db` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `blog_db`;

--
-- Table structure for table `aliases`
--

DROP TABLE IF EXISTS `aliases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aliases` (
  `id` int(10) unsigned NOT NULL,
  `name` varchar(50) NOT NULL,
  `host_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK_aliases_hosts` (`host_id`),
  CONSTRAINT `FK_aliases_hosts` FOREIGN KEY (`host_id`) REFERENCES `hosts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `archive_entries`
--

DROP TABLE IF EXISTS `archive_entries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `archive_entries` (
  `id` int(10) unsigned NOT NULL,
  `year` smallint(5) unsigned NOT NULL,
  `month` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `year_month` (`year`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `authors_hosts`
--

DROP TABLE IF EXISTS `authors_hosts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authors_hosts` (
  `author_id` int(10) unsigned NOT NULL,
  `host_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`author_id`,`host_id`),
  KEY `FK_authors_hosts_hosts` (`host_id`),
  CONSTRAINT `FK_authors_hosts_authors` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK_authors_hosts_hosts` FOREIGN KEY (`host_id`) REFERENCES `hosts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comments` (
  `id` int(10) unsigned NOT NULL,
  `text` varchar(250) NOT NULL,
  `visible` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `publication_date` bigint(20) unsigned NOT NULL,
  `last_modification_date` bigint(20) unsigned NOT NULL,
  `post_id` int(10) unsigned NOT NULL,
  `author_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_comments_users` (`author_id`),
  KEY `FK_comments_posts` (`post_id`),
  CONSTRAINT `FK_comments_posts` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `FK_comments_users` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `hosts`
--

DROP TABLE IF EXISTS `hosts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hosts` (
  `id` int(10) unsigned NOT NULL,
  `title` varchar(75) NOT NULL,
  `tagline` varchar(200) DEFAULT NULL,
  `paginate_index_page` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `posts_per_index_page` tinyint(3) unsigned NOT NULL DEFAULT '6',
  `archive_items_per_index_page` tinyint(3) unsigned NOT NULL DEFAULT '6',
  `popular_tags_per_index_page` tinyint(3) unsigned NOT NULL DEFAULT '6',
  `recent_comments_per_index_page` tinyint(3) unsigned NOT NULL DEFAULT '6',
  `feeds_max_post_age_in_days` smallint(5) unsigned NOT NULL DEFAULT '30',
  `active_theme_id` int(10) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `FK_hosts_themes` (`active_theme_id`),
  CONSTRAINT `FK_hosts_themes` FOREIGN KEY (`active_theme_id`) REFERENCES `themes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `pages`
--

DROP TABLE IF EXISTS `pages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pages` (
  `id` int(10) unsigned NOT NULL,
  `meta_description` varchar(250) DEFAULT NULL,
  `title` varchar(100) NOT NULL,
  `url_handler` varchar(50) NOT NULL,
  `content` varchar(3000) NOT NULL,
  `publication_date` bigint(20) unsigned NOT NULL,
  `last_modification_date` bigint(20) unsigned NOT NULL,
  `host_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `url_handler` (`url_handler`),
  KEY `FK_pages_hosts` (`host_id`),
  CONSTRAINT `FK_pages_hosts` FOREIGN KEY (`host_id`) REFERENCES `hosts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `posts` (
  `id` int(10) unsigned NOT NULL,
  `style` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `meta_description` varchar(250) DEFAULT NULL,
  `title` varchar(100) NOT NULL,
  `url_safe_title` varchar(50) NOT NULL,
  `excerpt` varchar(500) NOT NULL,
  `content` varchar(3000) NOT NULL,
  `header_image_url` varchar(100) DEFAULT NULL,
  `side_image_url` varchar(100) DEFAULT NULL,
  `comments_closed` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `publication_date` bigint(20) unsigned NOT NULL,
  `last_modification_date` bigint(20) unsigned NOT NULL,
  `year` smallint(5) unsigned NOT NULL,
  `month` tinyint(3) unsigned NOT NULL,
  `day` tinyint(3) unsigned NOT NULL,
  `host_id` int(10) unsigned NOT NULL,
  `author_id` int(10) unsigned NOT NULL,
  `archive_entry_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `url_safe_title` (`url_safe_title`),
  KEY `FK_posts_hosts` (`host_id`),
  KEY `FK_posts_authors` (`author_id`),
  KEY `FK_posts_archive_entries` (`archive_entry_id`),
  KEY `FK_posts_archive_entries_2` (`year`,`month`),
  CONSTRAINT `FK_posts_archive_entries` FOREIGN KEY (`archive_entry_id`) REFERENCES `archive_entries` (`id`),
  CONSTRAINT `FK_posts_archive_entries_2` FOREIGN KEY (`year`, `month`) REFERENCES `archive_entries` (`year`, `month`),
  CONSTRAINT `FK_posts_authors` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK_posts_hosts` FOREIGN KEY (`host_id`) REFERENCES `hosts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `posts_tags`
--

DROP TABLE IF EXISTS `posts_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `posts_tags` (
  `post_id` int(10) unsigned NOT NULL,
  `tag_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`post_id`,`tag_id`),
  KEY `FK_posts_tags_tags` (`tag_id`),
  CONSTRAINT `FK_posts_tags_posts` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `FK_posts_tags_tags` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `sequence_table`
--

DROP TABLE IF EXISTS `sequence_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequence_table` (
  `seq` char(8) NOT NULL,
  `val` bigint(20) NOT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tags` (
  `id` int(10) unsigned NOT NULL,
  `name` varchar(50) NOT NULL,
  `url_handler` varchar(20) NOT NULL,
  `ref_count` mediumint(8) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `url_handler` (`url_handler`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `themes`
--

DROP TABLE IF EXISTS `themes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `themes` (
  `id` int(10) unsigned NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(10) unsigned NOT NULL,
  `user_type` char(1) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `about` varchar(250) DEFAULT NULL,
  `relatedUrl` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;



/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-04-06 17:31:00
