USE `likelabs`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `phone` VARCHAR(20) NOT NULL,
     `password` VARCHAR(20) NOT NULL,
     `is_active` TINYINT(1) NOT NULL,
     `email` VARCHAR(40),
     `system_admin` TINYINT(1) NOT NULL,
     `publish_in_sn` TINYINT(1) NOT NULL,
     `created_dt` DATETIME NOT NULL,
     `notified_dt` DATETIME,
     CONSTRAINT `PK_user` PRIMARY KEY (`id`),
     CONSTRAINT `UC_user_phone` UNIQUE(`phone`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user_social_account`;
CREATE TABLE `user_social_account` (
     `user_id` BIGINT NOT NULL,
     `type` TINYINT NOT NULL,
     `account_id` VARCHAR(20) NOT NULL,
     `access_token` VARCHAR(255),
     `name` VARCHAR(100) NOT NULL,
     CONSTRAINT `PK_user_social_account` PRIMARY KEY (`user_id`, `type`),
     CONSTRAINT `FK_user_social_account_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user_event_type`;
CREATE TABLE `user_event_type` (
     `user_id` BIGINT NOT NULL,
     `event_type` TINYINT NOT NULL,
     CONSTRAINT `PK_user_event_type` PRIMARY KEY (`user_id`, `event_type`),
     CONSTRAINT `FK_user_event_type_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `photo`;
CREATE TABLE `photo` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `image` BLOB NOT NULL,
     `created_dt` DATETIME NOT NULL,
     `status` TINYINT NOT NULL,
     `user_id` BIGINT NOT NULL,
     CONSTRAINT `PK_photo` PRIMARY KEY (`id`),
     CONSTRAINT `FK_photo_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
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

DROP TABLE IF EXISTS `company_social_page`;
CREATE TABLE `company_social_page` (
     `company_id` BIGINT NOT NULL,
     `type` TINYINT NOT NULL,
     `page_id` VARCHAR(20) NOT NULL,
     `url` VARCHAR(100) NOT NULL,
     CONSTRAINT `PK_company_social_page` PRIMARY KEY (`company_id`, `type`, `page_id`),
     CONSTRAINT `FK_company_social_page_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `company_admin`;
CREATE TABLE `company_admin` (
     `user_id` BIGINT NOT NULL,
     `company_id` BIGINT NOT NULL,
     CONSTRAINT `PK_company_admin` PRIMARY KEY (`user_id`, `company_id`),
     CONSTRAINT `FK_company_admin_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
     CONSTRAINT `FK_company_admin_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `point`;
CREATE TABLE `point` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `phone` VARCHAR(20) NOT NULL,
     `email` VARCHAR(40) NOT NULL,
     `company_id` BIGINT NOT NULL,
     CONSTRAINT `PK_point` PRIMARY KEY (`id`),
     CONSTRAINT `FK_point_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `point_address`;
CREATE TABLE `point_address` (
     `point_id` BIGINT NOT NULL,
     `city` VARCHAR(80) NOT NULL,
     `state` VARCHAR(80) NOT NULL,
     `postal_code` VARCHAR(40),
     `country` VARCHAR(80) NOT NULL,
     `address_line_1` VARCHAR(80) NOT NULL,
     `address_line_2` VARCHAR(80),
     CONSTRAINT `PK_point_address` PRIMARY KEY (`point_id`),
     CONSTRAINT `FK_point_address_point` FOREIGN KEY (`point_id`) REFERENCES `point` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `tablet`;
CREATE TABLE `tablet` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `login` VARCHAR(20) NOT NULL,
     `login_password` VARCHAR(20) NOT NULL,
     `logout_password` VARCHAR(20) NOT NULL,
     `api_key` VARCHAR(32) NOT NULL,
     `point_id` BIGINT NOT NULL,
     CONSTRAINT `PK_tablet` PRIMARY KEY (`id`),
     CONSTRAINT `UC_tablet_login` UNIQUE(`login`),
     CONSTRAINT `FK_tablet_point` FOREIGN KEY (`point_id`) REFERENCES `point` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `message` TEXT,
     `photo_id` BIGINT,
     `author_id` BIGINT NOT NULL,
     `created_dt` DATETIME NOT NULL,
     `modified_dt` DATETIME,     
     `status` TINYINT NOT NULL,
     `published_in_company_sn` TINYINT(1) NOT NULL,
     `moderator_id` BIGINT,
     `moderated_dt` DATETIME,
     `point_id` BIGINT NOT NULL,
     CONSTRAINT `PK_review` PRIMARY KEY (`id`),
     CONSTRAINT `FK_review_point` FOREIGN KEY (`point_id`) REFERENCES `point` (`id`) ON DELETE CASCADE,
     CONSTRAINT `FK_review_photo` FOREIGN KEY (`photo_id`) REFERENCES `photo` (`id`) ON DELETE SET NULL,
     CONSTRAINT `FK_review_author` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
     CONSTRAINT `FK_review_moderator` FOREIGN KEY (`moderator_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `review_recipient`;
CREATE TABLE `review_recipient` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `is_notified` TINYINT(1) NOT NULL,
     `review_id` BIGINT NOT NULL,
     CONSTRAINT `PK_recipient` PRIMARY KEY (`id`),
     CONSTRAINT `FK_recipient_review` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `review_sms_recipient`;
CREATE TABLE `review_sms_recipient` (
     `recipient_id` BIGINT AUTO_INCREMENT NOT NULL,
     `phone` VARCHAR(20) NOT NULL,
     CONSTRAINT `PK_sms_recipient` PRIMARY KEY (`recipient_id`),
     CONSTRAINT `FK_sms_recipient_recipient` FOREIGN KEY (`recipient_id`) REFERENCES `review_recipient` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `review_email_recipient`;
CREATE TABLE `review_email_recipient` (
     `recipient_id` BIGINT AUTO_INCREMENT NOT NULL,
     `email` VARCHAR(40) NOT NULL,
     CONSTRAINT `PK_email_recipient` PRIMARY KEY (`recipient_id`),
     CONSTRAINT `FK_email_recipient_recipient` FOREIGN KEY (`recipient_id`) REFERENCES `review_recipient` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sample_review`;
CREATE TABLE `sample_review` (
     `review_id` BIGINT NOT NULL,
     `company_id` BIGINT NOT NULL,
     CONSTRAINT `PK_sample_review` PRIMARY KEY (`review_id`, `company_id`),
     CONSTRAINT `FK_sample_review_review` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`) ON DELETE CASCADE,
     CONSTRAINT `FK_sample_review_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `event`;
CREATE TABLE `event` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `type` TINYINT NOT NULL,
     `created_dt` DATETIME NOT NULL,
     `notified_dt` DATETIME,
     `user_id` BIGINT NOT NULL,
     `review_id` BIGINT NOT NULL,
     CONSTRAINT `PK_event` PRIMARY KEY (`id`),
     CONSTRAINT `FK_event_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
     CONSTRAINT `FK_event_review` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `notification_interval`;
CREATE TABLE `notification_interval` (
     `id` BIGINT AUTO_INCREMENT NOT NULL,
     `event_type` TINYINT NOT NULL,
     `warning_type` TINYINT NOT NULL,
     `email_interval` INT NOT NULL,
     `sms_interval` INT NOT NULL,
     CONSTRAINT `PK_notification_interval` PRIMARY KEY (`id`)
) ENGINE=InnoDB, DEFAULT CHARSET=utf8;


