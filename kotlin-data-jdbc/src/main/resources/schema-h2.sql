
CREATE SCHEMA IF NOT EXISTS `kotest` ;
USE `kotest` ;


CREATE TABLE IF NOT EXISTS `kotest`.`purchase` (
  `purchase_no` BIGINT NOT NULL AUTO_INCREMENT,
  `addr` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`purchase_no`));

CREATE TABLE IF NOT EXISTS `kotest`.`order` (
    `order_no` BIGINT NOT NULL AUTO_INCREMENT,
    `purchase_no` BIGINT NOT NULL,
    `product` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`order_no`),
    INDEX `fk_order_purchase1_idx` (`purchase_no` ASC));

