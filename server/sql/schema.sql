USE `likelabs`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT AUTO_INCREMENT NOT NULL,
    `phone` VARCHAR(20) NOT NULL,
    `password` VARCHAR(20) NOT NULL,
    `email` VARCHAR(40),
    `is_system_admin` TINYINT(1) NOT NULL,
    `notify_about_comments` TINYINT(1) NOT NULL,
    `created_dt` DATETIME NOT NULL,
    CONSTRAINT `PK_user_id` PRIMARY KEY (`id`),
    CONSTRAINT `UC_user_phone` UNIQUE(`phone`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user_social_account`;
CREATE TABLE `user_social_account` (
    `id` BIGINT AUTO_INCREMENT NOT NULL,
    `type` TINYINT NOT NULL,
    `account_id` VARCHAR(100) NOT NULL,
    `post_message` TINYINT(1) NOT NULL,
    `register_location` TINYINT(1) NOT NULL,
    `user_id` BIGINT NOT NULL,
    CONSTRAINT `PK_user_social_account_id` PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_social_account_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user_image`;
CREATE TABLE `user_image` (
    `id` BIGINT AUTO_INCREMENT NOT NULL,
    `image` BLOB NOT NULL,
    `user_id` BIGINT NOT NULL,
    CONSTRAINT `PK_user_photo_id` PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_photo_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
    `id` BIGINT AUTO_INCREMENT NOT NULL,
    `name` VARCHAR(80) NOT NULL,
    `phone` VARCHAR(20) NOT NULL,
    `email` VARCHAR(40) NOT NULL,
    `moderation_type` TINYINT NOT NULL,
    CONSTRAINT `PK_company_id` PRIMARY KEY (`id`),
    CONSTRAINT `UC_company_name` UNIQUE(`name`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `company_social_account`;
CREATE TABLE `company_social_account` (
    `id` BIGINT AUTO_INCREMENT NOT NULL,
    `type` TINYINT NOT NULL,
    `account_id` VARCHAR(100) NOT NULL,
    `post_message` TINYINT(1) NOT NULL,
    `company_id` BIGINT NOT NULL,
    CONSTRAINT `PK_company_social_account_id` PRIMARY KEY (`id`),
    CONSTRAINT `FK_company_social_account_company_id` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `company_address`;
CREATE TABLE `company_address` (
    `id` BIGINT AUTO_INCREMENT NOT NULL,
    `city` VARCHAR(80),
    `state` VARCHAR(80),
    `postal_code` VARCHAR(40),
    `country` VARCHAR(80),
    `address_line_1` VARCHAR(80),
    `address_line_2` VARCHAR(80),
    `company_id` BIGINT NOT NULL,
    CONSTRAINT `PK_company_address_id` PRIMARY KEY (`id`),
    CONSTRAINT `UC_company_address_company_id` UNIQUE(`company_id`),
    CONSTRAINT `FK_company_address_company_id` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `company_admin`;
CREATE TABLE `company_admin` (
    `user_id` BIGINT NOT NULL,
    `company_id` BIGINT NOT NULL,
    CONSTRAINT `PK_company_admin` PRIMARY KEY (`user_id`, `company_id`),
    CONSTRAINT `FK_company_admin_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK_company_admin_company_id` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `point`;
CREATE TABLE `point` (
    `id` BIGINT AUTO_INCREMENT NOT NULL,
    `phone` VARCHAR(20) NOT NULL,
    `company_id` BIGINT NOT NULL,
    `tablet_login` VARCHAR(20) NOT NULL,
    `tablet_password` VARCHAR(20) NOT NULL,
    CONSTRAINT `PK_point_id` PRIMARY KEY (`id`),
    CONSTRAINT `FK_point_company_id` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `point_social_account`;
CREATE TABLE `point_social_account` (
    `id` BIGINT AUTO_INCREMENT NOT NULL,
    `type` TINYINT NOT NULL,
    `account_id` VARCHAR(100) NOT NULL,
    `post_message` TINYINT(1) NOT NULL,
    `point_id` BIGINT NOT NULL,
    CONSTRAINT `PK_point_social_account_id` PRIMARY KEY (`id`),
    CONSTRAINT `FK_point_social_account_point_id` FOREIGN KEY (`point_id`) REFERENCES `point` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `point_address`;
CREATE TABLE `point_address` (
    `id` BIGINT AUTO_INCREMENT NOT NULL,
    `city` VARCHAR(80),
    `state` VARCHAR(80),
    `postal_code` VARCHAR(40),
    `country` VARCHAR(80),
    `address_line_1` VARCHAR(80),
    `address_line_2` VARCHAR(80),
    `point_id` BIGINT NOT NULL,
    CONSTRAINT `PK_point_address_id` PRIMARY KEY (`id`),
    CONSTRAINT `UC_point_address_point_id` UNIQUE(`point_id`),
    CONSTRAINT `FK_point_address_point_id` FOREIGN KEY (`point_id`) REFERENCES `point` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `notification_receiver`;
CREATE TABLE `notification_receiver` (
    `user_id` BIGINT NOT NULL,
    `point_id` BIGINT NOT NULL,
    CONSTRAINT `PK_notification_receiver` PRIMARY KEY (`user_id`, `point_id`),
    CONSTRAINT `FK_notification_receiver_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK_notification_receiver_point_id` FOREIGN KEY (`point_id`) REFERENCES `point` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
    `id` BIGINT AUTO_INCREMENT NOT NULL,
    `point_id` BIGINT NOT NULL,
    `author_id` BIGINT,
    `created_dt` DATETIME,
    `message` TEXT NOT NULL,
    `image` BLOB,
    `is_approved` TINYINT(1) NOT NULL,
    `moderator_id` BIGINT,
    `moderated_dt` DATETIME,
    `modifier_id` BIGINT,
    `modified_dt` DATETIME,
    CONSTRAINT `PK_review_id` PRIMARY KEY (`id`),
    CONSTRAINT `FK_review_point_id` FOREIGN KEY (`point_id`) REFERENCES `point` (`id`),
    CONSTRAINT `FK_review_author_id` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK_review_moderator_id` FOREIGN KEY (`moderator_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK_review_modifier_id` FOREIGN KEY (`modifier_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
    `id` BIGINT AUTO_INCREMENT NOT NULL,
    `review_id` BIGINT NOT NULL,
    `author_id` BIGINT,
    `created_dt` DATETIME,
    `message` TEXT NOT NULL,
    `is_approved` TINYINT(1) NOT NULL,
    `moderator_id` BIGINT,
    `moderated_dt` DATETIME,
    `modifier_id` BIGINT,
    `modified_dt` DATETIME,
    CONSTRAINT `PK_comment_id` PRIMARY KEY (`id`),
    CONSTRAINT `FK_comment_review_id` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`),
    CONSTRAINT `FK_comment_author_id` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK_comment_moderator_id` FOREIGN KEY (`moderator_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK_comment_modifier_id` FOREIGN KEY (`modifier_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

