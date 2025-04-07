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
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS `meme_tag`;
CREATE TABLE `meme_tag` (
  `meme_id` INT NOT NULL,
  `tag_id` INT NOT NULL,
  PRIMARY KEY (`meme_id`, `tag_id`)
);

DROP TABLE IF EXISTS `meme_vote`;
CREATE TABLE `meme_vote` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `meme_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT `fk_meme_vote_meme` FOREIGN KEY (`meme_id`) REFERENCES `meme`(`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_meme_vote_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  CONSTRAINT `unique_meme_user_vote` UNIQUE (`meme_id`, `user_id`)
);