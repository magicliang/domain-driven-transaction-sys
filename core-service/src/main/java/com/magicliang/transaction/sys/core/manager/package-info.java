/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 一般的 manager，数据管理器
 * 这是一种语义上的防腐层，实现了基于接口的解耦
 * 也提供了对 dao 等 dal 组件的事务性包装
 * 传统的 DDD 的结构事务边界是在用例层实现的，暂时先把本服务的事务边界放在这一层
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-04 14:05
 */

package com.magicliang.transaction.sys.core.manager;