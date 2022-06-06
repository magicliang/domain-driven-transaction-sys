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
    `id`                        BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增物理主键，单表唯一',
    -- 创建时间如果有可能由业务生成，否则使用缺省值
    `gmt_created`               DATETIME               DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    -- 修改时间如果有可能由业务生成，否则使用缺省值
    `gmt_modified`              DATETIME               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '最后修改时间',
    -- 本表业务主键，业务主键也不要忘记 UNSIGNED
    `pay_order_no`              BIGINT(20) UNSIGNED NOT NULL COMMENT '支付订单号，业务主键，全局唯一',
    -- 上游系统的 role 说明符，如果存在其他 role 建模，则这里可以是 role 的外键
    -- char 类型超过 16 个字符则考虑 varchar 吧
    -- 只有字符串适合 NOT NULL DEFAULT ''，其他都要酌情考虑-要考虑引入 magic number，magic string。缺省空值 NOT NULL DEFAULT '' 在语义上等于没有这个设置
    `sys_code`                  VARCHAR(255)  NOT NULL DEFAULT '' COMMENT '支付订单来源系统',
    -- 上游联合外键
    `biz_identify_no`           VARCHAR(255)  NOT NULL COMMENT '业务标识码',
    `biz_unique_no`             VARCHAR(255)  NOT NULL COMMENT '上游业务号，语义同 out_biz_no，与业务标识码联合后，必须全局唯一，可以作为分表键',
    `money`                     BIGINT(20) UNSIGNED NOT NULL COMMENT '支付单金额，单位为分，全为正数',
    -- 这种布尔/枚举类型使用 SMALLINT(6) 对存储更友好，不过对 ORM 不友好，暂时先使用 INT(6)。不要使用 bit、enum 和 set 类型，这些类型对存储的帮助不大，对 alter 的要求很高，也不易读（人工读或者 ORM）
    -- 本次交易因子，类似 description、place、party、thing 等坐标（coordination）
    `pay_channel_type`          INT(6) NOT NULL COMMENT '支付通道类型，1 支付宝 2 微信支付',
    `target_account_type`       INT(6) NOT NULL COMMENT '目标账户类型，1 银行卡 2 账户余额',
    `accounting_entry`          INT(6) NOT NULL COMMENT '会计账目条目 1 借 debit 2 贷 credit',
    -- 不可空时间，前置事务里有这个字段则必填
    `gmt_accepted_time`         DATETIME               DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '受理时间',
    -- 可空的时间，不用 default null 也可以，毕竟事件没发生不要触发约束索引。
    -- 能够使用在这里的时间应该是：只发生一次的事件的状态+时间可以建模在一个可空列里，每次事件发生可空列被填充。如果发生复杂变化需要考虑对于 domain event/status change log 的建模
    -- 不要轻易考虑 '0000-00-00 00:00:00' 这样的缺省值，这样的缺省值对 JDBC 数据类型不友好
    -- DATETIME not null 的 default value 是 '1970-01-01 00:00:00'
    `gmt_payment_begin_time`    DATETIME COMMENT '支付时间',
    `gmt_payment_success_time`  DATETIME COMMENT '支付成功时间',
    `gmt_payment_failure_time`  DATETIME COMMENT '支付失败时间',
    `gmt_payment_closed_time`   DATETIME COMMENT '支付关闭时间',
    `gmt_payment_bounced_time`  DATETIME COMMENT '支付退票时间',
    `status`                    INT(6) NOT NULL COMMENT '支付状态，1 初始化 2 支付中 3 支付成功 4 支付失败 5 关闭 6 支付退票',
    -- version 类型的字段需要配有缺省值
    `version`                   INT(20) UNSIGNED NOT NULL DEFAULT 1 COMMENT '版本',
    `memo`                      VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '支付备注',
    -- 发生一个事件就会产生一个流水号，一个正向流动会产生一个流水号，一个逆向流动会产生一个流水号，有多少个流动就有多少个流水号。如果事件必然在 rpc 以前发生，则本字段初始情况下即非空
    `channel_payment_trace_no`  VARCHAR(255)  NOT NULL DEFAULT '' COMMENT '支付通道的支付流水号，单一通道必须全局唯一',
    `channel_dishonor_trace_no` VARCHAR(255)  NOT NULL DEFAULT '' COMMENT '支付通道的退票流水号，单一通道必须全局唯一',
    -- 所有 rpc 必备的两个字段
    `channel_error_code`        VARCHAR(255)  NOT NULL DEFAULT '' COMMENT '支付错误码',
    `business_entity`           VARCHAR(255)  NOT NULL DEFAULT '' COMMENT '支付主体，上游系统选填',
    `notify_uri`                VARCHAR(255)  NOT NULL DEFAULT '' COMMENT '通知地址',
    `extend_info`               VARCHAR(2048) NOT NULL DEFAULT '' COMMENT '扩展信息，平台能力抽象，用于日后本服务特定的平台能力和流程打标用，不透传到链路的其他系统中，必须是 json 格式',
    `biz_info`                  VARCHAR(2048) NOT NULL DEFAULT '' COMMENT '扩展信息，业务能力抽象，透传到链路的其他系统中，本系统不理解，必须是 json 格式',
    `env`                       INT(6) UNSIGNED NOT NULL COMMENT '环境，1 dev 2 test 3 staging 4 prod',
    PRIMARY KEY (`id`),
    -- 建索引的几个维度：
    -- 非唯一类索引：1. 状态维度（只适于搜索少数状态），2. 时间维度（适于任务调度）建索引。
    -- 唯一类业务主键维度：1. 本业务的业务主键维度（需要大规模生成算法生成吗？生成算法唯一吗？全局唯一吗？唯一可以考虑建立，但有一天迁入 NewSql 会产生奇怪的问题）
    --                 2. 下游业务主键维度（收单唯一性）：可以由数字-名字空间-第三类型（唯一的业务、唯一的事件、唯一的关系、唯一的关系）作联合索引，注意，如果做了这样的配置，则唯一性索引易去难加。如果不在建表的时候就考虑好业务的唯一性，加唯一性索引会导致数据损毁（truncate）的。
    --                 3. 上游业务索引：可以由数字-名字空间-业务类型作联合索引。注意，2 和 3 都要考虑数字之外的名字空间（更大的索引）、第三类型-细分的子单支持。
    -- 不要使用长列作索引，尤其是 text 和 blob
    UNIQUE KEY `uniq_pay_order_no` (`pay_order_no`) COMMENT '唯一索引，每个支付单只能拥有唯一的支付单号',
    UNIQUE KEY `uniq_biz_request` (`biz_unique_no`, `biz_identify_no`) COMMENT '唯一索引，每种支付业务类型每个业务单只能有唯一的支付订单',
    KEY `idx_status_modified` (`status`, `gmt_modified`) COMMENT '查询索引，注意数据规模较大时区分度不高，慎用，，如果有可能一定要加上查询时间约束查询范围。如果有可能还是借助小的从表来进行状态查询。如果按照时间排序很可能触发回表进入 sort buffer'
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT='支付订单，领域聚合根';
-- utf8mb4 四字节才能容纳完整 utf8 字符集，COLLATE 意味着特定的排序算法
-- AUTO_INCREMENT 不用配太大，制造数据空洞
-- 同一个库允许不同的表使用不同的 engin，不同的 engine 会得到不同的表空间文件

DROP TABLE IF EXISTS `tb_trans_bank_card_suborder`;
CREATE TABLE `tb_trans_bank_card_suborder`
(
    `id`              BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增物理主键，单表唯一',
    `gmt_created`     DATETIME              DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `gmt_modified`    DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '最后修改时间',
    `pay_order_no`    BIGINT(20) UNSIGNED NOT NULL COMMENT '引用 tb_trans_pay_order 支付订单号，业务主键，全局唯一',
    -- 这个模型保存了差异化地描述银行账户的若干个信息
    `to_bank_account` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '目标银行账户',
    `account_type`    INT(6) NOT NULL COMMENT '支付账户类型，1 对公 2 对私',
    `name`            VARCHAR(255) NOT NULL DEFAULT '' COMMENT '目标银行账户户名',
    -- 数字类型就不适合使用 default 0 之类的值了，不如使用空好
    `bank_id`         INT(20) UNSIGNED COMMENT '银行 id',
    `branch_id`       INT(20) UNSIGNED COMMENT '支行 id',
    `city_id`         INT(20) UNSIGNED COMMENT '城市 id',
    `branch_name`     VARCHAR(255) NOT NULL DEFAULT '' COMMENT '支行 id',
    `to_branch_no`    VARCHAR(255) NOT NULL DEFAULT '' COMMENT '收款支行联行号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_pay_order_no` (`pay_order_no`) COMMENT '每个支付单只能拥有一个银行卡余额子订单'
    -- 因为本子订单无业务主键，可以考虑本实体的非生成的独立标识，也比如本模型使用 to_bank_account
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT='银行卡子订单，引用 tb_trans_pay_order';

DROP TABLE IF EXISTS `tb_trans_alipay_suborder`;
CREATE TABLE `tb_trans_alipay_suborder`
(
    `id`            BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增物理主键，单表唯一',
    `gmt_created`   DATETIME              DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `gmt_modified`  DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '最后修改时间',
    `pay_order_no`  BIGINT(20) UNSIGNED NOT NULL COMMENT '引用 tb_trans_pay_order 支付订单号，业务主键，全局唯一',
    `to_account_no` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '目标账户',
    `name`          VARCHAR(255) NOT NULL DEFAULT '' COMMENT '账户',
    -- fixme：增加业务 id
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_pay_order_no` (`pay_order_no`) COMMENT '每个支付单只能拥有一个支付宝余额子订单'
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT='支付宝子订单，引用 tb_trans_pay_order';

DROP TABLE IF EXISTS `tb_trans_channel_request`;
CREATE TABLE `tb_trans_channel_request`
(
    `id`                 BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增物理主键，单表唯一',
    `gmt_created`        DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `gmt_modified`       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '最后修改时间',
    `pay_order_no`       BIGINT(20) UNSIGNED NOT NULL COMMENT '引用 tb_trans_pay_order 支付订单号，业务主键，全局唯一',
    `request_type`       INT(6) UNSIGNED NOT NULL COMMENT '请求类型',
    `biz_identify_no`    VARCHAR(255)                       NOT NULL COMMENT '业务标识码',
    `biz_unique_no`      VARCHAR(255)                       NOT NULL COMMENT '上游业务号，语义同 out_biz_no，与业务标识码联合后，必须全局唯一，可以作为分表键',
    `request_params`     TEXT COMMENT '请求参数',
    `request_response`   TEXT COMMENT '请求响应',
    `callback_params`    TEXT COMMENT '回调参数',
    `request_exception`  TEXT COMMENT '请求异常',
    `gmt_next_execution` DATETIME COMMENT '下次预定执行时间，任务的优先级调度逻辑会间接影响这一列，任务调度框架主要理解这一列',
    `retry_count`        BIGINT(20) UNSIGNED NOT NULL COMMENT '调用次数',
    `request_addr`       VARCHAR(255)                       NOT NULL COMMENT '请求地址',
    `status`             INT(6) UNSIGNED NOT NULL COMMENT '支付状态，1 初始化 2 请求中 3 请求成功 4 请求失败 5请求被关闭',
    `close_reason`       INT(6) UNSIGNED COMMENT '请求被关闭的原因',
    `gmt_last_execution` DATETIME COMMENT '上次执行时间',
    `env`                INT(6) UNSIGNED NOT NULL COMMENT '环境，1 dev 2 test 3 staging 4 prod',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_pay_order_request` (`pay_order_no`, `request_type`) COMMENT '每个支付单每个类型只能存在一个请求',
    KEY                  `unq_biz_request` (`biz_unique_no`, `biz_identify_no`) COMMENT '根据业务标识码和上游业务号查询通道请求',
    KEY                  `idx_next_execution_status` (`gmt_next_execution`,`status`) COMMENT '查询索引，注意数据规模较大时区分度不高，慎用。'
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT='支付请求表，引用 tb_trans_pay_order';
