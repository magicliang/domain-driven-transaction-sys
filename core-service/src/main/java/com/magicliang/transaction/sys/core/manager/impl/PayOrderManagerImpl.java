package com.magicliang.transaction.sys.core.manager.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.magicliang.transaction.sys.common.dal.mybatis.mapper.TransChannelRequestPoMapper;
import com.magicliang.transaction.sys.common.dal.mybatis.mapper.TransPayOrderPoMapper;
import com.magicliang.transaction.sys.common.dal.mybatis.po.*;
import com.magicliang.transaction.sys.common.enums.TransPayOrderStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestTypeEnum;
import com.magicliang.transaction.sys.core.manager.PayOrderManager;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付订单管理器，带有事务管理功能
 * FIXME：补完这个类
 *
 * @author magicliang
 * <p>
 * date: 2022-01-04 14:19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PayOrderManagerImpl implements PayOrderManager {

    /**
     * 缺省批次大小，用于 in 之类的查询时，每次 in 不超过 1000 个值
     * 这个内部准备的尺寸大小可以有效地保护 MySQL
     * 如果有必要，可以动态配置化这个值
     */
    private static final int DEFAULT_BATCH = 1000;

    /**
     * payOrderMapper
     */
    @Autowired
    private TransPayOrderPoMapper payOrderMapper;

    /**
     * requestPoMapper
     */
    @Autowired
    private TransChannelRequestPoMapper requestPoMapper;

    /**
     * 根据支付订单号列表查询支付订单
     *
     * @param payOrderNos 支付订单号列表
     * @return 支付订单列表
     */
    @Override
    public List<TransPayOrderPo> queryPayOrders(final List<Long> payOrderNos) {
        // 防御性编程：单一的大列表查询要做好分区查询，甚至并行查询的准备
        return partitionQuery(payOrderNos, (nos) -> {
            TransPayOrderPoExample example = new TransPayOrderPoExample();
            TransPayOrderPoExample.Criteria criteria = example.createCriteria();
            criteria.andPayOrderNoIn(nos);
            return payOrderMapper.selectByExample(example);
        });
    }

    /**
     * 根据业务标识码 + 业务唯一标识 查询支付订单
     *
     * @param bizIdentifyNo 业务标识码
     * @param bizUniqueNo   业务唯一标识
     * @return 支付订单
     */
    @Override
    public TransPayOrderPo queryPayOrder(final String bizIdentifyNo, final String bizUniqueNo) {
        TransPayOrderPoExample example = new TransPayOrderPoExample();
        TransPayOrderPoExample.Criteria criteria = example.createCriteria();
        criteria.andBizIdentifyNoEqualTo(bizIdentifyNo);
        criteria.andBizUniqueNoEqualTo(bizUniqueNo);
        List<TransPayOrderPo> transPayOrderPos = payOrderMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(transPayOrderPos)) {
            // 根据阿里代码规范，检查空值是上游的责任，对于 Po 类型的模型，我们最好直接使用 null 而不是空对象了
            return null;
        }
        return transPayOrderPos.get(0);
    }

    /**
     * 本方法已废弃
     * <p>
     * 查询特定环境、全部的未完成的支付订单。
     * 因为这个查询可能查出大量数据，所以实现上使用了分页查询方法，为了查出全部的数据，将先查出总的数量，分多次查询全部数据。
     * <p>
     * 这种任务、订单类查询一定要区分环境！
     *
     * @param batchSize 每次查询的批次大小
     * @param env       环境
     * @return 未完成的支付订单列表
     */
    @Deprecated
    @Override
    public List<TransPayOrderPo> queryUnpaidPayOrder(final int batchSize, final int env) {
        // 对于状态等可能查出空列表或者大量数据的场景，要先 count，然后再翻页查询。所以设计这种复杂查询要仔细评估潜在数据量的变化，底层的 ORM 框架的返回结果的缺省行为、通过正交组合和分步处理来生成兼容性查询框架
        TransPayOrderPoExample example = createUnpaidPayOrderExample(env);
        long count = payOrderMapper.countByExample(example);
        if (count <= 0) {
            return Collections.emptyList();
        }
        return paginationQuery(count, batchSize, () -> payOrderMapper.selectByExample(example));
    }

    /**
     * 查询特定环境、最重要（通常是创建时间最早的订单）一批订单未完成的支付订单。
     * 因为限制了批次大小，
     * <p>
     * 这种任务、订单类查询一定要区分环境！
     *
     * @param env 环境
     * @return 最重要一批订单未完成的支付订单
     */
    @Override
    public List<TransPayOrderPo> queryTopBatchUnpaidPayOrder(final int env) {
        TransPayOrderPoExample example = createUnpaidPayOrderExample(env);
        return paginationQuery(DEFAULT_BATCH, DEFAULT_BATCH, () -> payOrderMapper.selectByExample(example));
    }


    /**
     * 获取当前所有未支付请求的数量
     *
     * @param env 环境
     * @return 当前所有未支付请求的数量
     */
    @Override
    public long countUnPaidRequests(final int env) {
        TransChannelRequestPoExample unPaidRequestExample = createUnPaidRequestExample(env);
        return requestPoMapper.countByExample(unPaidRequestExample);
    }

    /**
     * 查询特定环境、特定数量的未完成的支付请求
     *
     * @param batchSize 批次大小
     * @param env       环境
     * @return 未完成的支付请求列表
     */
    @Override
    public List<TransChannelRequestPoWithBLOBs> queryUnpaidPaymentRequest(final int batchSize, final int env) {
        return Collections.emptyList();
    }

    /**
     * 获取当前所有未发送通知的数量
     *
     * @return 当前所有未发送通知的数量
     */
    @Override
    public long countUnSentNotifications() {
        return 0;
    }

    /**
     * 查询特定环境、全部的未发送的通知请求列表
     *
     * @param batchSize 批次大小
     * @param env       环境
     * @return 未发送的通知请求列表
     */
    @Override
    public List<TransChannelRequestPoWithBLOBs> queryUnsentNotifications(final int batchSize, final int env) {
        return Collections.emptyList();
    }

    /**
     * 根据支付订单号查询支付宝子订单
     *
     * @param payOrderNo 支付订单号
     * @return 子订单列表
     */
    @Override
    public List<TransAlipaySubOrderPo> queryAlipaySubOrder(final Long payOrderNo) {
        return Collections.emptyList();
    }

    /**
     * 根据支付订单号查询支付宝子订单
     *
     * @param payOrderNos 支付订单号列表
     * @return 子订单列表
     */
    @Override
    public List<TransAlipaySubOrderPo> queryAlipaySubOrder(List<Long> payOrderNos) {
        return Collections.emptyList();
    }

    /**
     * 根据支付订单号查询支付请求
     *
     * @param payOrderNo 支付订单号
     * @return 支付请求列表
     */
    @Override
    public List<TransChannelRequestPoWithBLOBs> queryPaymentRequest(final Long payOrderNo) {
        return Collections.emptyList();
    }

    /**
     * 根据支付订单号查询通知请求
     *
     * @param payOrderNo 支付订单号
     * @return 支付通知列表
     */
    @Override
    public List<TransChannelRequestPoWithBLOBs> queryNotificationRequest(final Long payOrderNo) {
        return Collections.emptyList();
    }

    /**
     * 插入支付订单和支付请求
     *
     * @param payOrder       支付订单
     * @param request        支付请求
     * @param alipaySuborder 支付宝子订单
     * @return 支付结果
     */
    @Override
    public boolean insertPayOrder(final TransPayOrderPo payOrder, final TransChannelRequestPoWithBLOBs request, final TransAlipaySubOrderPo alipaySuborder) {
        return true;
    }

    /**
     * 插入支付订单和支付请求
     *
     * @param payOrder 支付订单
     * @param request  支付请求
     * @return 支付结果
     */
    @Override
    public boolean insertPayOrder(final TransPayOrderPo payOrder, final TransChannelRequestPoWithBLOBs request) {
        // 先插入前导对象，再插入聚合根、再插入子对象，让所有起到幂等、和唯一性约束的模型先插入数据库中
        return true;
    }

    /**
     * 在一个事务里更新支付订单和支付请求
     *
     * @param payOrder            支付订单
     * @param payRequest          支付请求
     * @param notificationRequest 通知请求
     * @return 支付结果
     */
    @Override
    public boolean insertNotificationAndUpdatePayOrder(final TransPayOrderPo payOrder, final TransChannelRequestPoWithBLOBs payRequest,
                                                       final TransChannelRequestPoWithBLOBs notificationRequest) {
        if (updatePayOrderAndRequest(payOrder, payRequest)) {
            return insertRequest(notificationRequest);
        }
        return false;
    }

    /**
     * 插入支付通道请求
     *
     * @param notificationRequest 待插入请求
     * @return 插入结果
     */
    @Override
    public boolean insertRequest(final TransChannelRequestPoWithBLOBs notificationRequest) {
//        int retR = requestMapper.insertSelective(notificationRequest);
//        DbUtils.checkDbInsertExpectedOne(retR, notificationRequest);
        return true;
    }

    /**
     * 在一个事务里更新支付订单和支付请求
     *
     * @param payOrder 支付订单
     * @param request  支付请求
     * @return 支付结果
     */
    @Override
    public boolean updatePayOrderAndRequest(final TransPayOrderPo payOrder, final TransChannelRequestPoWithBLOBs request) {
        if (updatePayOrder(payOrder)) {
            return updateChannelRequest(request);
        }
        return false;
    }


    /**
     * 更新支付订单，在更新过程中对比并增加版本
     *
     * @param payOrder 支付订单
     * @return 更新结果
     */
    @Override
    public boolean updatePayOrder(final TransPayOrderPo payOrder) {
        // 更新的时候要仔细区别生成更新条件列和更新结果列的区别
        // 也要理解affectedRows，理解并发事务、版本和 aba 问题，如果发生 aba 问题，要及时通过 DbUtils 回滚
        // 要明确每次更新一定变更的是什么，哪些列的变化是独一无二的：version、timestamp、status（终态或者不可循环的单向状态）
        // 生成支付订单更新条件 createUpdatePayOrderExample
        // 在一个事务里更新订单 updateByExampleSelective
//        DbUtils.checkDbUpdateExpectedOne(retR, payOrder);
        return true;
    }

    /**
     * 更新通道请求
     *
     * @param request 通道请求
     * @return 更新结果
     */
    @Override
    public boolean updateChannelRequest(final TransChannelRequestPoWithBLOBs request) {
        return true;
    }

    /**
     * 根据支付订单号列表查询通道请求列表
     *
     * @param payOrderNos 支付订单号列表
     * @return 通道请求列表
     */
    @Override
    public List<TransChannelRequestPoWithBLOBs> queryNotificationRequests(List<Long> payOrderNos) {
        return Collections.emptyList();
    }

    /**
     * 根据支付订单号来查询特定类型的结果
     * 分区查询，这样每次查询的时候输入大小可控
     *
     * @param payOrderNos 支付订单号列表
     * @param query       查询语句
     * @param <T>         返回值类型
     * @return 特定类型的结果
     */
    private <T> List<T> partitionQueryByPayOrderNos(final List<Long> payOrderNos, QueryByLongList<List<T>> query) {
        if (CollectionUtils.isEmpty(payOrderNos)) {
            return Collections.emptyList();
        }
        final int size = payOrderNos.size();
        List<T> result = Lists.newArrayListWithCapacity(size);

        // 向上取整计算总页数，此处不能用 Math.toIntExact
        int pageCount = (int) Math.ceil((double) size / DEFAULT_BATCH);
        // 迭代翻页
        IntStream.rangeClosed(1, pageCount).forEachOrdered(pageIndex -> {
            // 取出此页里的所有支付订单号

            // 算出起始下标，从 0 开始，如 0、1000、2000
            int beginIndex = (pageIndex - 1) * DEFAULT_BATCH;
            // 算出终止下标（下一页的起点），如 1000、2000、3000
            int endIndex = pageCount * DEFAULT_BATCH;
            List<Long> subList = Lists.newArrayListWithCapacity(DEFAULT_BATCH);
            for (int i = beginIndex; i < endIndex && i < size; i++) {
                subList.add(payOrderNos.get(i));
            }

            // 应用查询
            result.addAll(query.apply(subList));
        });
        return result;
    }

    /**
     * 根据特定条件分页查询
     * 注意这里做的查询是把 totalSize 里的所有页都查过去一遍
     *
     * @param totalSize 总大小
     * @param batchSize 批大小
     * @param query     查询回调
     * @return 分页查询结果
     */
    private <T> List<T> paginationQuery(final long totalSize,
                                        final int batchSize,
                                        final Supplier<List<T>> query) {
        // 查询条件不合法，则返回空列表
        if (totalSize <= 0 || batchSize <= 0) {
            return Collections.emptyList();
        }
        // 否则，开始翻页
        List<T> results = Lists.newArrayListWithCapacity(Math.toIntExact(totalSize));
        // 向上取整计算总页数
        int pageCount = (int) Math.ceil((double) totalSize / batchSize);
        // 迭代翻页
        IntStream.rangeClosed(1, pageCount).forEachOrdered(pageIndex -> {
            // 这个查询是有缺陷的，如果 pageIndex 很大，最后一次查询的结果可能要在 sort buffer 里面存储和丢弃非常多的临时数据，甚至引发文件排序，解决这个问题的方法是不允许总的翻页数过多
            PageHelper.startPage(pageIndex, batchSize);
            results.addAll(new ArrayList<>(query.get()));
        });
        return results;
    }

    /**
     * 根据分区函数执行基于 payOrderNos 的分页查询
     *
     * @param longList 支付订单号列表
     * @param query    查询语句
     * @param <T>      返回值类型
     * @return 分页查询结果
     */
    private <T> List<T> partitionQuery(final List<Long> longList, QueryByLongList<List<T>> query) {
        if (CollectionUtils.isEmpty(longList)) {
            return Collections.emptyList();
        }
        /*
         * 翻页查询的秘诀：每次只查询一批特别小的页，每次翻页的上限一定要小，避免：1. 单个查询时间太久，导致 innodb 的工作被阻塞 2. 单个连接被单个事务占用太久
         * 实现分页逻辑和非分页逻辑的分离，然后把非分页逻辑装进分页闭包里。
         *
         * 另一种不优雅的做法是 longList/DEFAULT_BATCH 向上取整得到总页数 pages，for( page = 0; page < pages; page++) { for(i = 0 + (page - 1) * DEFAULT_BATCH; i< page * DEFAULT_BATCH && i < totalCount); i++ }
         */
        List<List<Long>> partition = Lists.partition(longList, DEFAULT_BATCH);
        // 先分区查询，再 flatMap 和 collect，如果这个地方性能不好，就直接 foreach addAll可能会性能会更好
        // 如果有必要，这里加一个线程池，并行化 Stream 查询
        return partition.stream().map(query)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * 查询未支付订单的 example
     *
     * @param env 环境枚举值
     * @return 未支付订单的 example
     */
    private TransPayOrderPoExample createUnpaidPayOrderExample(int env) {
        TransPayOrderPoExample example = new TransPayOrderPoExample();
        TransPayOrderPoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusIn(TransPayOrderStatusEnum.UNPAID_STATUS_VALUE);
        criteria.andEnvEqualTo(env);
        /*
         * 索引的数据顺序和索引在查找中的排列顺序一致则获得二星。因为查找排序顺序是先按照 status 取数据，再按照时间排序，时间在status里查到的数据未必是有序的，所以把 gmt_modified 加入联合索引里无助于
         * 用搜索即排序加速，如果有可能加速，就是托了二级索引里有数据无需回表查主索引再进入排序缓冲区的福（使用把所有的select 和 order by 都扔进 sort buffer的方式工作）。
         * 这个地方如果有很强的性能要求，只能寄望于 UNPAID_STATUS_VALUE 相关的数据量非常小，发送索引跳跃的概率小
         * 虽然没有办法利用索引加速排序，但此处加上一个排序列还是有必要的，防止待支付订单饥饿
         */
        example.setOrderByClause("gmt_modified asc");
        return example;
    }

    /**
     * 创建查询所有的未支付请求列表的查询条件
     * 四大任务查询约束条件：状态、时间、环境、limit或者 id 区间
     * 时间：时间范围，时间排序？如果这两者都可以指定，则时间作为联合索引的第一个列是简单的
     *
     * @param env 环境
     * @return 查询所有的未支付请求列表的查询条件
     */
    private TransChannelRequestPoExample createUnPaidRequestExample(final int env) {
        TransChannelRequestPoExample example = new TransChannelRequestPoExample();
        TransChannelRequestPoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusIn(TransRequestStatusEnum.UNSENT_STATUS_VALUE);
        criteria.andRequestTypeEqualTo(TransRequestTypeEnum.PAYMENT.getCode());
        criteria.andEnvEqualTo(env);
        // 先不指定排序列和 limit
        return example;
    }

    /**
     * 生成未发送的通知请求列表的查询条件
     *
     * @return 未发送的通知请求列表的查询条件
     */
    private TransChannelRequestPoExample createUnsentNotificationsExample(final int env) {
        TransChannelRequestPoExample example = new TransChannelRequestPoExample();
        TransChannelRequestPoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusIn(TransRequestStatusEnum.UNSENT_STATUS_VALUE);
        // FIXME：怎么给请求建模
        criteria.andRequestTypeIn(TransRequestTypeEnum.NOTIFICATION_TYPE_VALUE);
        criteria.andEnvEqualTo(env);
        return example;
    }

    /**
     * 生成支付订单更新条件
     *
     * @param payOrder 支付订单
     * @return 支付订单更新条件
     */
    private Object createUpdatePayOrderExample(final TransPayOrderPo payOrder) {
        // 生成基础 example
        // 业务主键、幂等主键、版本 match
        return null;
    }

    /**
     * 查询指定支付订单和指定类型的通道请求
     *
     * @param payOrderNo   支付订单号
     * @param requestTypes 通道请求类型
     * @return 通道请求
     */
    private Object createRequestExampleForPayOrder(final Long payOrderNo, final Short... requestTypes) {
        // 生成基础 example
        // 业务主键、幂等主键、版本 match
        return null;
    }

    /**
     * 根据支付订单号来查询特定类型的结果，用于小表查询
     * 这个设计可以支持在本类内把查询转化为 lambda 表达式
     *
     * @param <T> 特定类型的结果
     */
    private interface QueryByLongList<T> extends Function<List<Long>, T> {
    }
}
