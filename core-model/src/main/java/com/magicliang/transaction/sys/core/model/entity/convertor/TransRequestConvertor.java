package com.magicliang.transaction.sys.core.model.entity.convertor;

import com.magicliang.transaction.sys.common.dal.mybatis.po.TransChannelRequestPoWithBLOBs;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 交易请求转换器
 *
 * @author magicliang
 * <p>
 * date: 2022-01-04 16:57
 */
public class TransRequestConvertor {

    /**
     * 私有构造器
     */
    private TransRequestConvertor() {
        throw new UnsupportedOperationException();
    }

    /**
     * 从领域模型转化为持久层对象
     *
     * @param request 领域模型
     * @return 持久层对象
     */
    public static TransChannelRequestPoWithBLOBs toPo(TransRequestEntity request) {
        return null;
    }

    /**
     * 从持久层对象转化为领域模型
     *
     * @param request 持久层对象
     * @return 领域模型
     */
    public static TransRequestEntity toDomainEntity(TransChannelRequestPoWithBLOBs request) {
        return null;
    }
}
