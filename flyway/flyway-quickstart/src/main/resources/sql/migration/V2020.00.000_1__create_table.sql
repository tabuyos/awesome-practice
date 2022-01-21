CREATE TABLE `test_flyway_table`
(
    `id`   bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `time` datetime   NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
