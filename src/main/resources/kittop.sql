USE kittop;

DROP TABLE kittop.user;
SELECT * FROM kittop.user;
CREATE TABLE kittop.user (
    `userId`        BIGINT NOT NULL AUTO_INCREMENT,
    `email`         VARCHAR(50) NOT NULL,
    `password`      VARCHAR(255) NOT NULL,
    `nickName`      VARCHAR(100) NOT NULL,
    `name`          VARCHAR(100) NOT NULL,
    `birth`         VARCHAR(100) NOT NULL,
    `gender`        CHAR(1) NOT NULL,
    `addr`          VARCHAR(100) NOT NULL,
    `phone`         VARCHAR(100) NOT NULL,
    `provider`      VARCHAR(100),
    `providerId`    VARCHAR(100),
    `regDate`       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updateDate`    TIMESTAMP,
    `uuid`          VARCHAR(50) DEFAULT NULL,
    `role`          VARCHAR(40) NOT NULL,
    PRIMARY KEY (`userId`),
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
    UNIQUE INDEX `nickName_UNIQUE` (`nickName` ASC) VISIBLE,
    UNIQUE INDEX `phone_UNIQUE` (`phone` ASC) VISIBLE,
    UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC) VISIBLE
);

DROP TABLE kittop.item;
SELECT * FROM kittop.item;
CREATE TABLE kittop.item (
    `itemId`        BIGINT NOT NULL AUTO_INCREMENT,
    `category`      VARCHAR(20) NOT NULL,
    `itemName`      VARCHAR(50) NOT NULL,
    `price`         INT NOT NULL,
    `stock`         INT NOT NULL DEFAULT 0,
    `hit`           INT NOT NULL DEFAULT 0,
    `content`       TEXT NOT NULL,
    `imgName`       VARCHAR(100) NOT NULL,
    `regDate`       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updateDate`    TIMESTAMP,
    PRIMARY KEY (`itemId`)
);

DROP TABLE kittop.cart;
SELECT * FROM kittop.cart;
CREATE TABLE kittop.cart (
    `cartId` BIGINT NOT NULL AUTO_INCREMENT,
    `userEmail` VARCHAR(50) NOT NULL,
    `itemId` BIGINT NOT NULL,
    PRIMARY KEY (`cartId`),
    INDEX `cart_userEmail_FK_idx` (`userEmail` ASC) VISIBLE,
    INDEX `cart_itemId_FK_idx` (`itemId` ASC) VISIBLE,
    CONSTRAINT `cart_userEmail_FK`
    FOREIGN KEY (`userEmail`)
    REFERENCES `kittop`.`user` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `cart_itemId_FK`
    FOREIGN KEY (`itemId`)
    REFERENCES `item` (`itemId`)
#     UNIQUE INDEX `itemId_UNIQUE` (`itemId` ASC) VISIBLE
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

DROP TABLE kittop.orderlist;
SELECT * FROM kittop.orderlist;
CREATE TABLE kittop.orderlist (
    `orderId` BIGINT NOT NULL AUTO_INCREMENT,
    `itemId` BIGINT NOT NULL,
    `count` INT NOT NULL,
    `userEmail` VARCHAR(50) NOT NULL,
    `addr` VARCHAR(100) NOT NULL,
    `request` VARCHAR(100),
    `orderer` VARCHAR(255) NOT NULL,
    `receiver` VARCHAR(50) NOT NULL,
    `ordererPhone` VARCHAR(100) NOT NULL,
    `receiverPhone` VARCHAR(100) NOT NULL,
    `regDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `status` VARCHAR(10) NOT NULL,
    `totalPrice` INT NOT NULL,
    `payment` VARCHAR(100) NOT NULL,
    `tossOrderId` VARCHAR(100) NOT NULL,
    `tossMethod` VARCHAR(10) NOT NULL,
    `tossBank` VARCHAR(10) NOT NULL,
    `reviewId` BIGINT NULL,
    `boardId` BIGINT NULL,
    PRIMARY KEY (`orderId`)
);

DROP TABLE kittop.board;
SELECT * FROM kittop.board;
CREATE TABLE kittop.board (
    `boardId` BIGINT NOT NULL AUTO_INCREMENT,
    `category` VARCHAR(10) NOT NULL,
    `title` VARCHAR(50) NOT NULL,
    `content` VARCHAR(500) NOT NULL,
    `userEmail` VARCHAR(50) NOT NULL,
    `regDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updateDate` TIMESTAMP,
    `orderId`    bigint null,
    PRIMARY KEY (`boardId`)
);

DROP TABLE kittop.review;
SELECT * FROM kittop.review;
create table kittop.review
(
    reviewId  bigint auto_increment
        primary key,
    title     varchar(50)                         not null,
    content   varchar(500)                        not null,
    userEmail varchar(50)                         not null,
    regDate   timestamp default CURRENT_TIMESTAMP not null,
    itemId    bigint                              not null,
    orderId   bigint                              null,
    constraint review_itemId_FK
        foreign key (itemId) references item (itemId)
            on update cascade on delete cascade
)
    charset = utf8mb3;

create index review_itemId_FK_idx
    on kittop.review (itemId);

DROP TABLE kittop.ripple;
SELECT * FROM kittop.ripple;
CREATE TABLE kittop.ripple (
    `rippleId` BIGINT NOT NULL AUTO_INCREMENT,
    `content` VARCHAR(500) NOT NULL,
    `regDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `boardId` BIGINT NOT NULL,
    PRIMARY KEY (`rippleId`),
    INDEX `ripple_boardId_FK_idx` (`boardId` ASC) VISIBLE,
    CONSTRAINT `ripple_boardId_FK`
    FOREIGN KEY (`boardId`)
    REFERENCES `board` (`boardId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);