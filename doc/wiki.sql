# 电子书表
DROP TABLE IF EXISTS `ebook`;
CREATE TABLE `ebook`
(
    `id`           bigint NOT NULL COMMENT 'id',
    `name`         varchar(50) COMMENT '名称',
    `category1_id` bigint COMMENT '分类1',
    `category2_id` bigint COMMENT '分类2',
    `description`  varchar(200) COMMENT '描述',
    `cover`        varchar(200) COMMENT '封面',
    `doc_count`    int    NOT NULL DEFAULT 0 COMMENT '文档数',
    `view_count`   int    NOT NULL DEFAULT 0 COMMENT '阅读数',
    `vote_count`   int    NOT NULL DEFAULT 0 COMMENT '点赞数',
    PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = UTF8MB4 COMMENT ='电子书';

INSERT INTO `ebook` (id, name, description)
VALUES (1, 'Spring Boot 入门教程', '零基础入门 Java 开发，企业级应用开发最佳首选框架');
INSERT INTO `ebook` (id, name, description)
VALUES (2, 'Vue 入门教程', '零基础入门 Vue 开发，企业级应用开发最佳首选框架');
INSERT INTO `ebook` (id, name, description)
VALUES (3, 'Python 入门教程', '零基础入门 Python 开发，企业级应用开发最佳首选框架');
INSERT INTO `ebook` (id, name, description)
VALUES (4, 'Mysql 入门教程', '零基础入门 Mysql 开发，企业级应用开发最佳首选框架');
INSERT INTO `ebook` (id, name, description)
VALUES (5, 'Oracle 入门教程', '零基础入门 Oracle 开发，企业级应用开发最佳首选框架');

DROP TABLE IF EXISTS `test`;
CREATE TABLE `test`
(
    `id`       bigint NOT NULL COMMENT 'id',
    `name`     varchar(50) COMMENT '名称',
    `password` varchar(50) COMMENT '密码',
    PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = UTF8MB4 COMMENT ='测试';

INSERT INTO `test` (id, name, password)
VALUES (1, '测试', 'password');

# 分类
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`
(
    `id`     bigint      NOT NULL COMMENT 'id',
    `parent` bigint      NOT NULL DEFAULT 0 COMMENT '父id',
    `name`   varchar(50) NOT NULL COMMENT '名称',
    `sort`   int COMMENT '顺序',
    PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = UTF8MB4 COMMENT ='分类';

INSERT INTO `category` (id, parent, name, sort)
VALUES (100, 000, '前端开发', 100);
INSERT INTO `category` (id, parent, name, sort)
VALUES (101, 100, 'Vue', 101);
INSERT INTO `category` (id, parent, name, sort)
VALUES (102, 100, 'HTML & CSS', 102);
INSERT INTO `category` (id, parent, name, sort)
VALUES (200, 000, 'Java', 200);
INSERT INTO `category` (id, parent, name, sort)
VALUES (201, 200, '基础应用', 201);
INSERT INTO `category` (id, parent, name, sort)
VALUES (202, 200, '框架应用', 202);
INSERT INTO `category` (id, parent, name, sort)
VALUES (300, 000, 'Python', 300);
INSERT INTO `category` (id, parent, name, sort)
VALUES (301, 300, '基础应用', 301);
INSERT INTO `category` (id, parent, name, sort)
VALUES (302, 300, '进阶方向应用', 302);
INSERT INTO `category` (id, parent, name, sort)
VALUES (400, 000, '数据库', 400);
INSERT INTO `category` (id, parent, name, sort)
VALUES (401, 400, 'MySQL', 401);
INSERT INTO `category` (id, parent, name, sort)
VALUES (500, 000, '其它', 500);
INSERT INTO `category` (id, parent, name, sort)
VALUES (501, 500, '服务器', 501);
INSERT INTO `category` (id, parent, name, sort)
VALUES (502, 500, '开发工具', 502);
INSERT INTO `category` (id, parent, name, sort)
VALUES (503, 500, '热门服务端语言', 503);

-- 文档表
DROP TABLE IF EXISTS `doc`;
CREATE TABLE `doc`
(
    `id`         bigint      NOT NULL COMMENT 'id',
    `ebook_id`   bigint      NOT NULL DEFAULT 0 COMMENT '电子书id',
    `parent`     bigint      NOT NULL DEFAULT 0 COMMENT '父id',
    `name`       varchar(50) NOT NULL COMMENT '名称',
    `sort`       int COMMENT '顺序',
    `view_count` int                  DEFAULT 0 COMMENT '阅读数',
    `vote_count` int                  DEFAULT 0 COMMENT '点赞数',
    PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = UTF8MB4 COMMENT ='文档';

INSERT INTO `doc` (id, ebook_id, parent, name, sort, view_count, vote_count)
VALUES (1, 1, 0, '文档1', 1, 0, 0);
INSERT INTO `doc` (id, ebook_id, parent, name, sort, view_count, vote_count)
VALUES (2, 1, 1, '文档1.1', 1, 0, 0);
INSERT INTO `doc` (id, ebook_id, parent, name, sort, view_count, vote_count)
VALUES (3, 1, 0, '文档2', 2, 0, 0);
INSERT INTO `doc` (id, ebook_id, parent, name, sort, view_count, vote_count)
VALUES (4, 1, 3, '文档2.1', 1, 0, 0);
INSERT INTO `doc` (id, ebook_id, parent, name, sort, view_count, vote_count)
VALUES (5, 1, 3, '文档2.2', 2, 0, 0);
INSERT INTO `doc` (id, ebook_id, parent, name, sort, view_count, vote_count)
VALUES (6, 1, 5, '文档2.2.1', 1, 0, 0);

-- 文档内容
DROP TABLE IF EXISTS `content`;
CREATE TABLE `content`
(
    `id`      bigint     NOT NULL COMMENT '文档id',
    `content` mediumtext NOT NULL COMMENT '内容',
    PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = UTF8MB4 COMMENT ='文档内容';

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`         bigint      NOT NULL COMMENT 'ID',
    `login_name` varchar(50) NOT NULL COMMENT '登陆名',
    `name`       varchar(50) COMMENT '昵称',
    `password`   char(32)    NOT NULL COMMENT '密码',
    PRIMARY KEY (`id`),
    UNIQUE KEY `login_name_unique` (`login_name`)
) ENGINE = innodb
  DEFAULT CHARSET = UTF8MB4 COMMENT ='用户';

INSERT INTO `user` (id, `login_name`, `name`, `password`)
VALUES (1, 'test', '测试', 'e70e2222a9d67c4f2eae107533359aa4');

-- 电子书快照表
DROP TABLE IF EXISTS `ebook_snapshot`;
CREATE TABLE `ebook_snapshot`
(
    `id`            bigint AUTO_INCREMENT NOT NULL COMMENT 'id',
    `ebook_id`      bigint                NOT NULL DEFAULT 0 COMMENT '电子书id',
    `date`          date                  NOT NULL COMMENT '快照日期',
    `view_count`    int                   NOT NULL DEFAULT 0 COMMENT '阅读数',
    `vote_count`    int                   NOT NULL DEFAULT 0 COMMENT '点赞数',
    `view_increase` int                   NOT NULL DEFAULT 0 COMMENT '阅读增长',
    `vote_increase` int                   NOT NULL DEFAULT 0 COMMENT '点赞增长',
    PRIMARY KEY (`id`),
    UNIQUE KEY `ebook_id_date_unique` (`ebook_id`, `date`)
) ENGINE = innodb
  DEFAULT CHARSET = UTF8MB4 COMMENT ='电子书快照表';


DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo`
(
    `id`   bigint NOT NULL COMMENT 'id',
    `name` varchar(50) COMMENT '名称',
    PRIMARY KEY (`id`)
) ENGINE = innodb
  DEFAULT CHARSET = UTF8MB4 COMMENT ='测试';

INSERT INTO `demo` (id, name)
VALUES (1, '测试');