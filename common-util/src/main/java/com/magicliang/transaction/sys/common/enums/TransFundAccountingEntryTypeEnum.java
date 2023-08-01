package com.magicliang.transaction.sys.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 资金会计条目
 * https://www.accountingtools.com/articles/2017/5/17/debits-and-credits
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-31 16:13
 */
@Getter
@RequiredArgsConstructor
public enum TransFundAccountingEntryTypeEnum {

    /**
     * 借
     */
    DEBIT(1, "debit"),

    /**
     * 贷
     */
    CREDIT(2, "credit"),
    ;

    /**
     * 枚举类型码
     */
    private final Integer code;

    /**
     * 枚举类型描述
     */
    private final String desc;

    /**
     * 通过枚举类型码获取枚举
     *
     * @param code 枚举类型码
     * @return 枚举
     */
    public static TransFundAccountingEntryTypeEnum getByCode(Integer code) {
        if (null == code) {
            return null;
        }
        for (TransFundAccountingEntryTypeEnum value : TransFundAccountingEntryTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 通过枚举类型描述获取枚举
     *
     * @param desc 枚举类型描述
     * @return 枚举
     */
    public static TransFundAccountingEntryTypeEnum getByDesc(String desc) {
        if (StringUtils.isBlank(desc)) {
            return null;
        }
        for (TransFundAccountingEntryTypeEnum value : TransFundAccountingEntryTypeEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return null;
    }
}
