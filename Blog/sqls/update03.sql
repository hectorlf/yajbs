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
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `languages`;

CREATE TABLE `languages` (
  `id` int(10) unsigned NOT NULL,
  `language` varchar(3) NOT NULL,
  `region` varchar(3) NULL,
  `variant` varchar(8) NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `lang_reg_var` (`language`,`region`,`variant`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Datos de lenguajes por defecto
--

INSERT INTO `languages` (`id`, `language`, `region`) VALUES (1, 'es', 'ES');

--
-- Alter para la union de usuarios con lenguajes
--

ALTER TABLE `users`
	ADD COLUMN `language_id` INT(10) UNSIGNED NOT NULL DEFAULT '1' AFTER `relatedUrl`,
	ADD CONSTRAINT `FK_users_languages` FOREIGN KEY (`language_id`) REFERENCES `languages` (`id`);


	
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
