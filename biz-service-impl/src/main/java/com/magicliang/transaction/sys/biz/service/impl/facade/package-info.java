/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 内部门面包
 * <p>
 * facade 层存在的意义是，作为门面屏蔽了下层的实现 bus 或者 service。
 * 每一个 facade 是一套内聚的系统用例，底层一整套商业能力是通过一个一个 facade 建立起来并暴露出去的。
 * 每个 facade 未来可以有多个入口 rpc service impl、controller、mq job，etc。
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 16:36
 */
package com.magicliang.transaction.sys.biz.service.impl.facade;