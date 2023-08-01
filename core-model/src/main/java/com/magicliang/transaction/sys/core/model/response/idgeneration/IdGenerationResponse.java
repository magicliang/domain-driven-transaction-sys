package com.magicliang.transaction.sys.core.model.response.idgeneration;

import com.magicliang.transaction.sys.core.model.response.IResponse;
import java.util.List;
import lombok.Data;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: id 生成活动响应
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-31 17:21
 */
@Data
public class IdGenerationResponse implements IResponse {

    /**
     * 生成的 id
     */
    private List<Long> ids;
}
