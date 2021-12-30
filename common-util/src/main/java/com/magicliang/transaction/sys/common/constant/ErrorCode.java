package com.magicliang.transaction.sys.common.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 另一种 ErrorCode
 *
 * @author magicliang
 * <p>
 * date: 2021-12-30 16:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorCode implements Serializable {

    private static final long serialVersionUID = 6823814128684829881L;

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误类型
     */
    private String type;

    /**
     * 错误消息
     */
    private String message;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
