CREATE DATABASE IF NOT EXISTS `meme`;
USE `meme`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS `meme`;
CREATE TABLE `meme` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `url` VARCHAR(255) NOT NULL,
  `user_id` INT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `tags` VARCHAR(255) NOT NULL
);