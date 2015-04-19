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
/*!40101 SET character_set_client = utf8 */;


--
-- Current Database: `blog_db`
--

USE `blog_db`;

--
-- Bomba nuclear
--

ALTER TABLE `posts`
	DROP FOREIGN KEY `FK_posts_hosts`,
	DROP KEY `FK_posts_hosts`,
	DROP COLUMN `host_id`;

ALTER TABLE `pages`
	DROP FOREIGN KEY `FK_pages_hosts`,
	DROP KEY `FK_pages_hosts`,
	DROP COLUMN `host_id`;

ALTER TABLE `hosts`
	DROP FOREIGN KEY `FK_hosts_themes`,
	DROP KEY `FK_hosts_themes`,
	DROP COLUMN `active_theme_id`;

DROP TABLE IF EXISTS `aliases`;
DROP TABLE IF EXISTS `themes`;
DROP TABLE IF EXISTS `authors_hosts`;
DROP TABLE IF EXISTS `hosts`;


CREATE TABLE `preferences` (
  `id` int(10) unsigned NOT NULL,
  `title` varchar(75) NOT NULL,
  `tagline` varchar(200) DEFAULT NULL,
  `paginate_index_page` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `posts_per_index_page` tinyint(3) unsigned NOT NULL DEFAULT '6',
  `feeds_max_post_age_in_days` smallint(5) unsigned NOT NULL DEFAULT '30',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `preferences` VALUES (1, 'Las pataletas de Héctor', 'un blog sobre historias de terror y software', 0, 3, 30);


/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
