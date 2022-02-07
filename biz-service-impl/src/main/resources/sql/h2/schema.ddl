-- 阿里代码规范试图标准化建表语句为：create_time、updated_time
-- 关键字尽量大写，表名大小写敏感，数据库名、表名和字段名都小写，需要区分词意以“_”连接
-- 禁止使用关键字做为库、表、字段名,附MySQL保留关键字，不能作为表名和字段名
-- 表，字段名需要有明确意义，并简单明了
DROP TABLE IF EXISTS `tb_trans_pay_order`;
-- 本模型是为了说明事务+过程+实体，所以会包含上下游，又作为聚合根矗立在子域/限界上下文里。必备的字段：上游的身份和描述、下游的身份和描述、本 rpc 前后置信息（状态+版本+时间）、帮助环境治理的字段（env、cell、set、region、ldc 等）、如果有必要还要加上某些技术方案的版本字段（交易版本、加解密版本）

-- 所有的 transaction 包括：1 本地事务1 2 rpc 3 本地事务2。只有本地事务 1 能够生成且必须生成的信息是非空字段，它们是 rpc 和事务 2 的基础，如果理论上不一定在事务中生成的信息，才需要有缺省值。
CREATE TABLE `tb_trans_pay_order`
(
    -- 所有字段先考虑 not null，text/blob/json/geometry 类型除外
    -- 每个表必须带上的三个字段
    -- 一张表必须只有一个 AUTO_INCREMENT
    `id`                        BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`id`)
);
