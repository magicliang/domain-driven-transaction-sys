/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: FIXME：使用 JWT 和 oAuth
 * 在这一层使用的 dto 要满足如下特征：
 * <p>
 * 1. immutable：使用 final + 构造器 + builder 模式赋值
 * 2. 只是领域模型的片段
 * 3. 贫血模型：no behavior
 *
 * @author magicliang
 *         <p>
 *         date: 2022-05-25 13:57
 */

package com.magicliang.transaction.sys.biz.service.impl.web;