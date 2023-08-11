package com.magicliang.transaction.sys.common.exception;

import com.magicliang.transaction.sys.common.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 通用业务异常基类
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-05 15:28
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -5015868220699906377L;

    /**
     * 错误码
     */
    private ErrorCode errorCode;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 错误枚举
     */
    private Enum num;

    public BizException(String message) {
        super(message);
        this.message = message;
    }

    public BizException(ErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public BizException(ErrorCode errorCode, String message) {
        super(errorCode.toString() + "," + message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public BizException(ErrorCode errorCode, Throwable e) {
        super(errorCode.toString(), e);
        this.errorCode = errorCode;
    }

    public BizException(Enum num) {
        super(num.toString());
        this.num = num;
    }

    public BizException(Enum num, String message) {
        super(num.toString() + "," + message);
        this.message = message;
        this.num = num;
    }

    public BizException(Enum num, Throwable e) {
        super(num.toString(), e);
        this.num = num;
    }

    public BizException(Enum num, String message, Throwable e) {
        super(num.toString(), e);
        this.message = message;
        this.num = num;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }
}
