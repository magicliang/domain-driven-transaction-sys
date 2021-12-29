package com.magicliang.transaction.sys.common.exception;

import com.magicliang.transaction.sys.common.enums.TransErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 交易异常
 *
 * @author magicliang
 * <p>
 * date: 2021-12-29 11:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BaseTransException extends RuntimeException {

    /**
     * 错误码
     */
    private String errorCode = "";

    /**
     * 错误消息
     */
    private String errorMsg = "";

    /**
     * 错误码枚举
     */
    private TransErrorEnum errorEnum;

    /**
     * 是否可重试错误
     */
    private Boolean retryAble = false;

    /**
     * 构造器
     *
     * @param errorEnum 错误枚举
     */
    public BaseTransException(TransErrorEnum errorEnum) {
        this(null, errorEnum, null, null, null);
    }

    /**
     * 构造器
     *
     * @param cause     原因
     * @param errorEnum 错误枚举
     */
    public BaseTransException(Throwable cause, TransErrorEnum errorEnum) {
        this(cause, errorEnum, null, null, null);
    }

    /**
     * 构造器
     *
     * @param errorEnum 错误枚举
     * @param errorCode 自定义错误码
     * @param message   自定义错信息
     */
    public BaseTransException(TransErrorEnum errorEnum, String errorCode, String message) {
        this(null, errorEnum, errorCode, message, null);
    }

    /**
     * 构造器
     *
     * @param errorEnum 错误枚举
     * @param message   自定义错信息
     */
    public BaseTransException(TransErrorEnum errorEnum, String message) {
        this(null, errorEnum, null, message, null);
    }

    /**
     * 构造器
     *
     * @param cause     原因
     * @param errorEnum 错误枚举
     * @param message   自定义错信息
     */
    public BaseTransException(Throwable cause, TransErrorEnum errorEnum, String message) {
        this(cause, errorEnum, null, message, null);
    }

    /**
     * 构造器
     *
     * @param cause     原因
     * @param errorEnum 错误枚举
     * @param errorCode 自定义错误码
     * @param message   自定义错信息
     */
    public BaseTransException(final Throwable cause, final TransErrorEnum errorEnum, final String errorCode, final String message, final Boolean retryAble) {
        super(cause);
        setErrorEnum(errorEnum);
        // 1. 如果错误枚举不为空，设置其错误码和错误信息为本异常的缺省错误码和错误信息
        if (null != errorEnum) {
            setErrorCode(errorEnum.getSynthesizedErrorCode());
            setErrorMsg(errorEnum.getErrorMsg());
        }
        // 2. 设置自定义错误码
        if (StringUtils.isNotBlank(errorCode)) {
            setErrorCode(errorCode);
        }
        // 3. 设置自定义错信息
        if (StringUtils.isNotBlank(message)) {
            setErrorMsg(message);
        }

        if (null != retryAble) {
            setRetryAble(retryAble);
        }
    }
}
