package com.magicliang.transaction.sys.core.model.request.idgeneration;

import com.magicliang.transaction.sys.core.model.request.IRequest;
import lombok.Data;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: id 生成活动请求
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-31 17:27
 */
@Data
public class IdGenerationRequest implements IRequest {

    /**
     * id 生成用的种子，如 leaf 的 key
     */
    private String sequenceKey;

    /**
     * id 生成的批次大小
     */
    private int batchSize;

}
