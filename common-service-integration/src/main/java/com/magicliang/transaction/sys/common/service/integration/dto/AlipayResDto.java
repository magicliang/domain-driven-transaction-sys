package com.magicliang.transaction.sys.common.service.integration.dto;

import com.magicliang.transaction.sys.common.enums.AliPayResultStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付宝响应 dto
 *
 * @author magicliang
 * <p>
 * date: 2021-12-29 12:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlipayResDto {

    /**
     * 该次请求是否受理成功（仅代表这次请求是否受理成功，不代表付款成功）：
     * 0 代表本次受理成功
     * -1 代表受理失败，错误码明确失败则表示结果失败，错误码重试则表示业务方可发起重试
     *
     * @see AliPayResultStatusEnum
     */
    private Integer status;

    /**
     * 付款任务内部系统 id，仅在 status 为0时出现
     * <p>
     * 建议各个接入方在判断 status == 0 之后检查taskId是否为大于0的整数值。如果taskId <= 0，请联系下游团队检查问题；
     * <p>
     * 同时严格避免更换流水号重新发起付款（可能引起资损）。
     */
    private Integer taskId;

    /**
     * 错误码，仅在 status 为-1时出现
     */
    private String errorCode;

    /**
     * 错误信息，仅在 status 为-1时出现
     */
    private String errorMessage;

    /**
     * 实际构造器
     *
     * @param builder 建造器
     */
    private AlipayResDto(Builder builder) {
        setStatus(builder.status);
        setTaskId(builder.taskId);
        setErrorCode(builder.errorCode);
        setErrorMessage(builder.errorMessage);
    }

    /**
     * 静态建造器类
     */
    public static final class Builder {

        /**
         * 该次请求是否受理成功（仅代表这次请求是否受理成功，不代表付款成功）：
         * 0 代表本次受理成功
         * -1 代表受理失败，错误码明确失败则表示结果失败，错误码重试则表示业务方可发起重试
         */
        private Integer status;

        /**
         * 付款任务内部系统id，仅在status为0时出现
         * <p>
         * 必须判断 status == 0 之后检查 taskId 是否为大于0的整数值。如果 taskId <= 0，请联系付款团队检查问题；
         * <p>
         * 同时严格避免更换流水号重新发起付款（可能引起资损）。
         */
        private Integer taskId;

        /**
         * 错误码，仅在status为-1时出现
         */
        private String errorCode;

        /**
         * 错误信息，仅在status为-1时出现
         */
        private String errorMessage;

        /**
         * 空构造器
         */
        Builder() {
            // 空构造器
        }

        /**
         * 构造成功结果
         *
         * @param taskId 任务 id
         * @return 成功结果
         */
        public static AlipayResDto buildSuccessResult(Integer taskId) {
            return new Builder()
                    .status(AliPayResultStatusEnum.SUCCESS.getCode())
                    .taskId(taskId).build();
        }

        /**
         * 构造失败结果
         *
         * @param taskId       任务 id
         * @param errorCode    错误码
         * @param errorMessage 错误消息
         * @return 失败结果
         */
        public static AlipayResDto buildFailureResult(final Integer taskId, final String errorCode, final String errorMessage) {
            return new Builder()
                    .taskId(taskId)
                    .status(AliPayResultStatusEnum.FAILURE.getCode())
                    .errorCode(errorCode)
                    .errorMessage(errorMessage)
                    .build();
        }

        /**
         * 设置状态
         *
         * @param val 状态
         * @return builder
         */
        public Builder status(Integer val) {
            status = val;
            return this;
        }

        /**
         * 设置任务 id
         *
         * @param val 设置任务 id
         * @return builder
         */
        Builder taskId(Integer val) {
            taskId = val;
            return this;
        }

        /**
         * 设置错误码
         *
         * @param val 错误码
         * @return builder
         */
        Builder errorCode(String val) {
            errorCode = val;
            return this;
        }

        /**
         * 设置错误信息
         *
         * @param val 错误信息
         * @return builder
         */
        Builder errorMessage(String val) {
            errorMessage = val;
            return this;
        }

        /**
         * 构建
         *
         * @return 构造结果
         */
        AlipayResDto build() {
            return new AlipayResDto(this);
        }
    }
}
