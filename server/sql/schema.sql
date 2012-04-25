USE `likelabs`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `phone` VARCHAR(20) NOT NULL,
     `password` VARCHAR(20) NOT NULL,
     `email` VARCHAR(40),
     `is_active` TINYINT(1) NOT NULL,
     `is_system_admin` TINYINT(1) NOT NULL,
     `publish_reviews_in_sn` TINYINT(1) NOT NULL,
     `notify_if_client` TINYINT(1) NOT NULL,
     `created_dt` DATETIME NOT NULL,
     `activated_dt` DATETIME NOT NULL,
     `notified_dt` DATETIME NOT NULL,
     CONSTRAINT `PK_user` PRIMARY KEY (`id`),
     CONSTRAINT `UC_user_phone` UNIQUE(`phone`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user_social_account`;
CREATE TABLE `user_social_account` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `type` TINYINT NOT NULL,
     `name` VARCHAR(100) NOT NULL,
     `user_id` BIGINT NOT NULL,
     CONSTRAINT `PK_user_social_account` PRIMARY KEY (`id`),
     CONSTRAINT `UC_user_social_account_type_user` UNIQUE(`type`, `user_id`),
     CONSTRAINT `FK_user_social_account_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `photo`;
CREATE TABLE `photo` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `photo_data` BLOB NOT NULL,
     `status` TINYINT NOT NULL,
     `user_id` BIGINT NOT NULL,
     CONSTRAINT `PK_photo` PRIMARY KEY (`id`),
     CONSTRAINT `FK_photo_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `name` VARCHAR(80) NOT NULL,
     `phone` VARCHAR(20) NOT NULL,
     `email` VARCHAR(40) NOT NULL,
     `moderate_reviews` TINYINT(1) NOT NULL,
     `logo` BLOB,
     CONSTRAINT `PK_company` PRIMARY KEY (`id`),
     CONSTRAINT `UC_company_name` UNIQUE(`name`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `notification_interval`;
CREATE TABLE `notification_interval` (
     `company_id` BIGINT NOT NULL,
     `event_type` TINYINT NOT NULL,
     `notification_type` TINYINT NOT NULL,
     `interval_in_days` TINYINT,
     CONSTRAINT `PK_notification_interval` PRIMARY KEY (`company_id`),
     CONSTRAINT `UC_notification_interval` UNIQUE(`event_type`, `notification_type`),
     CONSTRAINT `FK_notification_interval_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `company_social_account`;
CREATE TABLE `company_social_account` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `type` TINYINT NOT NULL,
     `name` VARCHAR(100) NOT NULL,
     `company_id` BIGINT NOT NULL,
     CONSTRAINT `PK_company_social_account` PRIMARY KEY (`id`),
     CONSTRAINT `FK_company_social_account_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `company_admin`;
CREATE TABLE `company_admin` (
     `user_id` BIGINT NOT NULL,
     `company_id` BIGINT NOT NULL,
     CONSTRAINT `PK_company_admin` PRIMARY KEY (`user_id`, `company_id`),
     CONSTRAINT `FK_company_admin_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
     CONSTRAINT `FK_company_admin_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `point`;
CREATE TABLE `point` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `phone` VARCHAR(20) NOT NULL,
     `company_id` BIGINT NOT NULL,
     `tablet_login` VARCHAR(20) NOT NULL,
     `tablet_password` VARCHAR(20) NOT NULL,
     CONSTRAINT `PK_pointd` PRIMARY KEY (`id`),
     CONSTRAINT `FK_point_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
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
     CONSTRAINT `PK_point_address` PRIMARY KEY (`id`),
     CONSTRAINT `UC_point_address_point` UNIQUE(`point_id`),
     CONSTRAINT `FK_point_address_point` FOREIGN KEY (`point_id`) REFERENCES `point` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `tablet`;
CREATE TABLE `tablet` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `login` VARCHAR(20) NOT NULL,
     `login_password` VARCHAR(20) NOT NULL,
     `logout_password` VARCHAR(20) NOT NULL,
     `point_id` BIGINT NOT NULL,
     CONSTRAINT `PK_tablet` PRIMARY KEY (`id`),
     CONSTRAINT `FK_tablet_point` FOREIGN KEY (`point_id`) REFERENCES `point` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `point_id` BIGINT NOT NULL,
     `message` TEXT,
     `photo_id` BIGINT,
     `author_id` BIGINT NOT NULL,
     `created_dt` DATETIME NOT NULL,
     `modified_dt` DATETIME,     
     `status` TINYINT NOT NULL,
     `publish_in_company_sn` TINYINT(1) NOT NULL,
     `moderator_id` BIGINT,
     `moderated_dt` DATETIME,
     CONSTRAINT `PK_review` PRIMARY KEY (`id`),
     CONSTRAINT `FK_review_point` FOREIGN KEY (`point_id`) REFERENCES `point` (`id`),
     CONSTRAINT `FK_review_photo` FOREIGN KEY (`photo_id`) REFERENCES `photo` (`id`),
     CONSTRAINT `FK_review_author` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`),
     CONSTRAINT `FK_review_moderator` FOREIGN KEY (`moderator_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `recipient`;
CREATE TABLE `recipient` (
     `review_id` BIGINT AUTO_INCREMENT NOT NULL,
     `type` TINYINT(1) NOT NULL,
     `address` VARCHAR(40) NOT NULL,
     CONSTRAINT `PK_recipient` PRIMARY KEY (`review_id`),
     CONSTRAINT `FK_recipient_review` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sample_review`;
CREATE TABLE `sample_review` (
     `review_id` BIGINT NOT NULL,
     `company_id` BIGINT NOT NULL,
     CONSTRAINT `PK_sample_review` PRIMARY KEY (`review_id`, `company_id`),
     CONSTRAINT `FK_sample_review_review` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`),
     CONSTRAINT `FK_sample_review_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event`;
CREATE TABLE `event` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `type` TINYINT NOT NULL,
     `created_dt` DATETIME NOT NULL,
     `is_user_notified` TINYINT(1) NOT NULL,
     `user_id` BIGINT NOT NULL,
     `review_id` BIGINT NOT NULL,
     CONSTRAINT `PK_event` PRIMARY KEY (`id`),
     CONSTRAINT `FK_event_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
     CONSTRAINT `FK_event_review` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `system_parameter`;
CREATE TABLE `system_parameter` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `type` INT NOT NULL,
     `value` VARCHAR(100) NOT NULL,
     CONSTRAINT `PK_system_parameter` PRIMARY KEY (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

