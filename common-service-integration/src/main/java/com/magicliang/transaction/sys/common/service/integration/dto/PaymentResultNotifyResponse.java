package com.magicliang.transaction.sys.common.service.integration.dto;

import lombok.Data;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付通道支付结果回调响应
 *
 * @author magicliang
 * <p>
 * date: 2022-01-04 17:34
 */
@Data
public class PaymentResultNotifyResponse {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 建造
     *
     * @param builder 建造器
     */
    private PaymentResultNotifyResponse(final Builder builder) {
        setSuccess(builder.success);
        setErrorCode(builder.errorCode);
        setErrorMsg(builder.errorMsg);
    }

    /**
     * 生成建造器
     *
     * @return 建造器
     */
    public static Builder newBuilder() {
        return new Builder();
    }


    /**
     * {@code PaymentResultNotifyResponse} builder static inner class.
     */
    public static final class Builder {

        /**
         * 是否成功
         */
        private boolean success;

        /**
         * 错误码
         */
        private String errorCode;

        /**
         * 错误信息
         */
        private String errorMsg;

        /**
         * 构造器
         */
        private Builder() {
        }

        /**
         * 构造成功结果
         *
         * @return 成功结果
         */
        public static PaymentResultNotifyResponse buildSuccessResult() {
            return new Builder().withSuccess(true).build();
        }

        /**
         * 构造失败结果
         *
         * @param errorCode    错误码
         * @param errorMessage 错误消息
         * @return 失败结果
         */
        public static PaymentResultNotifyResponse buildFailureResult(final String errorCode, final String errorMessage) {
            return new Builder().withSuccess(false).withErrorCode(errorCode).withErrorMsg(errorMessage).build();
        }

        /**
         * Sets the {@code success} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code success} to set
         * @return a reference to this Builder
         */
        Builder withSuccess(final boolean val) {
            success = val;
            return this;
        }

        /**
         * Sets the {@code errorCode} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code errorCode} to set
         * @return a reference to this Builder
         */
        Builder withErrorCode(final String val) {
            errorCode = val;
            return this;
        }

        /**
         * Sets the {@code errorMsg} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code errorMsg} to set
         * @return a reference to this Builder
         */
        Builder withErrorMsg(final String val) {
            errorMsg = val;
            return this;
        }

        /**
         * Returns a {@code PaymentResultNotifyResponse} built from the parameters previously set.
         *
         * @return a {@code PaymentResultNotifyResponse} built with parameters of this {@code PaymentResultNotifyResponse.Builder}
         */
        PaymentResultNotifyResponse build() {
            return new PaymentResultNotifyResponse(this);
        }
    }
}
