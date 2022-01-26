package com.magicliang.transaction.sys.biz.shared.request.acceptance;

import com.magicliang.transaction.sys.biz.shared.enums.OperationEnum;
import com.magicliang.transaction.sys.biz.shared.request.HandlerRequest;
import com.magicliang.transaction.sys.common.enums.TransFundAccountingEntryTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 受理命令
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 14:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AcceptanceCommand extends HandlerRequest {

    /**
     * 支付单金额，单位为分，全为正数，上游系统必填
     */
    private Long money;

    /**
     * 会计账目条目 1 借 debit 2 贷 credit，上游系统必填
     *
     * @see TransFundAccountingEntryTypeEnum
     */
    private Short accountingEntry;

    /**
     * 支付备注，上游系统选填
     */
    private String memo;

    /**
     * 支付主体，上游系统选填
     */
    private String businessEntity;

    /**
     * 回调地址，上游系统必填
     */
    private String notifyUri;

    /**
     * 扩展信息，平台能力抽象，用于日后本服务特定的平台能力和流程打标用，不透传到链路的其他系统中，上游系统选填
     * 必须是 json 格式
     */
    private Map<String, String> extendInfo;

    /**
     * 扩展信息，业务能力抽象，透传到链路的其他系统中，本系统不理解，上游系统选填
     * 必须是 json 格式
     */
    private Map<String, String> bizInfo;

    /**
     * 标识自己的类型。
     * 未来可以绑定 type variable 到 enum
     *
     * @return 类型
     */
    @Override
    public OperationEnum identify() {
        return OperationEnum.ACCEPTANCE;
    }
}

