package com.magicliang.transaction.sys.core.domain.strategy.idgeneration;

import com.magicliang.transaction.sys.common.service.integration.delegate.sequence.ILeafServiceDelegate;
import com.magicliang.transaction.sys.core.domain.enums.IdGenerationStrategyEnum;
import com.magicliang.transaction.sys.core.domain.strategy.BaseStrategy;
import com.magicliang.transaction.sys.core.domain.strategy.DomainStrategy;
import com.magicliang.transaction.sys.core.model.request.idgeneration.IdGenerationRequest;
import com.magicliang.transaction.sys.core.model.response.idgeneration.IdGenerationResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: leaf id 生成策略
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-04 17:08
 */
@Slf4j
@Component
public class LeafIdGenerationStrategy extends BaseStrategy implements
        DomainStrategy<IdGenerationRequest, IdGenerationResponse, IdGenerationStrategyEnum> {

    /**
     * leaf 服务委托
     */
    @Autowired
    private ILeafServiceDelegate leafServiceDelegate;

    /**
     * 执行领域请求，生成领域响应
     *
     * @param idGenerationRequest 领域请求
     * @param idGenerationResponse 领域响应
     */
    @Override
    public void execute(final IdGenerationRequest idGenerationRequest,
            final IdGenerationResponse idGenerationResponse) {
        String sequenceKey = idGenerationRequest.getSequenceKey();
        int batchSize = idGenerationRequest.getBatchSize();
        List<Long> ids = leafServiceDelegate.getSnowflakeBatch(sequenceKey, batchSize);
        idGenerationResponse.setIds(ids);
    }

    /**
     * 标识自己的类型。
     *
     * @return 类型
     */
    @Override
    public IdGenerationStrategyEnum identify() {
        return IdGenerationStrategyEnum.LEAF;
    }
}
