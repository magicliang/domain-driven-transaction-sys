package com.magicliang.transaction.sys.common.enums;

import static com.magicliang.transaction.sys.common.enums.TransErrorMiddleTypeEnum.SECOND_BIZ;
import static com.magicliang.transaction.sys.common.enums.TransErrorMiddleTypeEnum.SECOND_SYS;
import static com.magicliang.transaction.sys.common.enums.TransErrorMiddleTypeEnum.SELF_BIZ;
import static com.magicliang.transaction.sys.common.enums.TransErrorMiddleTypeEnum.SELF_SYS;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 基本交易错误码枚举
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-29 11:58
 */
@Getter
@RequiredArgsConstructor
public enum TransErrorEnum {

    // ---------------------------- 本系统业务错误，如：0010100001 ----------------------------

    /**
     * 0010100001，缺省业务异常
     */
    DEFAULT_BIZ_ERROR(SELF_BIZ, "00001", "缺省业务异常", true),

    /**
     * 0010100002，不正确的操作
     */
    INVALID_OP_ERROR(SELF_BIZ, "00002", "不正确的操作", true),

    /**
     * 0010100003，幂等请求
     */
    IDEMPOTENT_REQUEST_ERROR(SELF_BIZ, "00003", "幂等请求", true),

    /**
     * 0010100004，不正确的参数
     */
    INVALID_PARAMETER_ERROR(SELF_BIZ, "00004", "不正确的参数", false),

    /**
     * 0010100005，不正确的响应
     */
    INVALID_RES_ERROR(SELF_BIZ, "00005", "不正确的响应", true),

    /**
     * 0010100006，不正确的模型
     */
    INVALID_MODEL_ERROR(SELF_BIZ, "00006", "不正确的模型", false),

    /**
     * 0010100007，不正确的支付订单
     */
    INVALID_PAY_ORDER_ERROR(SELF_BIZ, "00007", "不正确的支付订单", false),

    /**
     * 0010100008，不正确的支付订单号
     */
    INVALID_PAY_ORDER_NO_ERROR(SELF_BIZ, "00008", "不正确的支付订单id", false),

    /**
     * 0010100009，不正确的子支付订单
     */
    INVALID_SUB_ORDER_ERROR(SELF_BIZ, "00009", "不正确的支付子订单", false),

    /**
     * 0010100010，不正确的支付请求
     */
    INVALID_PAYMENT_REQUEST_ERROR(SELF_BIZ, "00010", "不正确的支付请求", false),

    /**
     * 0010100011，不正确的通知请求
     */
    INVALID_NOTIFICATION_REQUEST_ERROR(SELF_BIZ, "00011", "不正确的通知请求", false),

    /**
     * 0010100012，受理失败
     */
    ACCEPTANCE_FAILURE_ERROR(SELF_BIZ, "00012", "受理失败", true),

    /**
     * 0010100013，支付状态为失败
     */
    PAYMENT_FAILURE_ERROR(SELF_BIZ, "00013", "支付状态为失败", false),

    /**
     * 0010100014，支付被关闭
     */
    PAYMENT_CLOSED_ERROR(SELF_BIZ, "00014", "支付被关闭", false),

    /**
     * 0010100015，支付被退票
     */
    PAYMENT_BOUNCED_ERROR(SELF_BIZ, "00015", "支付被退票", false),

    /**
     * 0010100016，不正确的幂等键
     */
    INVALID_IDEMPOTENT_KEY_ERROR(SELF_BIZ, "00016", "不正确的幂等键", true),

    /**
     * 0010100017，不正确的 id 生成策略
     */
    INVALID_GENERATION_STRATEGY_ERROR(SELF_BIZ, "00017", "不正确的 id 生成策略", true),

    /**
     * 0010100018，不正确的受理策略
     */
    INVALID_ACCEPTANCE_STRATEGY_ERROR(SELF_BIZ, "00018", "不正确的受理策略", true),

    /**
     * 0010100019，不正确的支付策略
     */
    INVALID_PAYMENT_STRATEGY_ERROR(SELF_BIZ, "00019", "不正确的支付策略", true),

    /**
     * 0010100020，不正确的通知策略
     */
    INVALID_NOTIFICATION_STRATEGY_ERROR(SELF_BIZ, "00020", "不正确的通知策略", true),

    /**
     * 0010100021，不正确的支付通道流水号
     */
    INVALID_CHANNEL_PAYMENT_TRACE_NO_ERROR(SELF_BIZ, "00021", "不正确的支付通道流水号", true),

    /**
     * 0010100022，通知不成功
     */
    NOTIFICATION_FAILURE_ERROR(SELF_BIZ, "00022", "通知不成功", true),

    /**
     * 0010100023，不正确的支付订单状态
     */
    INVALID_PAY_ORDER_STATUS_ERROR(SELF_BIZ, "00023", "不正确的支付订单状态", true),

    /**
     * 0010100024，批量支付任务执行失败
     */
    BATCH_PAYMENT_JOB_ERROR(SELF_BIZ, "00024", "批量支付任务执行失败", true),

    /**
     * 0010100025，请求参数生成签名失败
     */
    GENERATE_SIGNATURE_ERROR(SELF_BIZ, "00025", "请求参数生成签名失败", false),

    /**
     * 0010100026，签名验证失败
     */
    CHECK_SIGNATURE_ERROR(SELF_BIZ, "00026", "签名验证失败", false),

    /**
     * 0010100027，批量通知任务执行失败
     */
    BATCH_NOTIFICATION_JOB_ERROR(SELF_BIZ, "00027", "批量通知任务执行失败", true),

    /**
     * 0010100028，不正确的通知 uri
     */
    INVALID_NOTIFY_URI(SELF_BIZ, "00028", "不正确的通知 uri", false),

    /**
     * 0010100029，不正确的通知请求状态
     */
    INVALID_CHANNEL_REQUEST_STATUS_ERROR(SELF_BIZ, "00029", "不正确的通知请求状态", false),

    /**
     * 0010100030，不正确的嵌入式db端口配置
     */
    INVALID_EMBEDDED_DB_PORT_ERROR(SELF_BIZ, "00030", "不正确的嵌入式 DB 端口配置", false),

    /**
     * 0010100031，无法启动嵌入式 DB
     */
    UNABLE_TO_BOOTSTRAP_EMBEDDED_DB_PORT_ERROR(SELF_BIZ, "00031", "无法启动嵌入式 DB", false),

    // ---------------------------- 本系统系统错误，如：0010200001 ----------------------------

    /**
     * 0010200001，缺省系统异常
     */
    DEFAULT_SYS_ERROR(SELF_SYS, "00001", "缺省系统异常", true),

    // ---------------------------- 第二方业务错误，如：0010300001 ----------------------------

    /**
     * 0010300001，leaf id 生成业务错误
     */
    LEAF_ID_GENERATION_BIZ_ERROR(SECOND_BIZ, "00001", "leaf id 生成业务错误", true),

    /**
     * 0010300002，支付系统付款可重试业务错误
     */
    PAYMENT_PLATFORM_BIZ_ERROR(SECOND_BIZ, "00002", "支付系统付款可重试业务错误", true),

    /**
     * 0010300004，支付系统付款可重试业务错误
     */
    PAYMENT_PLATFORM_BIZ_RETRYABLE_ERROR(SECOND_BIZ, "00004", "支付系统付款可重试业务错误", true),

    /**
     * 0010300005，支付系统付款错误的任务 id
     */
    PAYMENT_PLATFORM_INVALID_TASK_ID_ERROR(SECOND_BIZ, "00005", "支付系统付款错误的任务 id", true),

    /**
     * 0010300006，rpc 调用错误
     */
    INVOKE_RPC_ERROR(SECOND_BIZ, "00006", "rpc 调用错误", true),

    /**
     * 0010300007，不正确的分布式锁键
     */
    INVALID_DISTRIBUTE_LOCK_KEY_ERROR(SECOND_BIZ, "00007", "不正确的分布式锁键", false),

    /**
     * 0010300008，不正确的分布式锁超时时间
     */
    INVALID_DISTRIBUTE_LOCK_EXPIRATION_ERROR(SECOND_BIZ, "00008", "不正确的分布式锁超时时间", false),

    /**
     * 0010300009，B端钱包付款可重试业务错误
     */
    M_WALLET_BIZ_ERROR(SECOND_BIZ, "00009", "B端钱包付款可重试业务错误", true),

    /**
     * 0010300010，B端钱包付款非法状态
     */
    M_WALLET_INVALID_STATUS(SECOND_BIZ, "00010", "B端钱包付款非法状态", false),

    /**
     * 0010300011，支付系统任务状态错误
     */
    PAYMENT_PLATFORM_INVALID_TASK_STATUS_ERROR(SECOND_BIZ, "00011", "支付系统任务状态错误", false),

    // ---------------------------- 第二方系统错误，如：0010400001 ----------------------------

    /**
     * 0010400001，插入数据库错误
     */
    DB_INSERT_ERROR(SECOND_SYS, "00001", "插入数据库错误", true),

    /**
     * 0010400002，删除数据库错误
     */
    DB_DELETE_ERROR(SECOND_SYS, "00002", "删除数据库错误", true),

    /**
     * 0010400003，更新数据库错误
     */
    DB_UPDATE_ERROR(SECOND_SYS, "00003", "更新数据库错误", true),

    /**
     * 0010400004，查询数据库错误
     */
    DB_SELECT_ERROR(SECOND_SYS, "00004", "查询数据库错误", true),

    /**
     * 0010400005，leaf id生成，TE错误
     */
    LEAF_ID_GENERATION_TE_ERROR(SECOND_SYS, "00005", "leaf id生成，TE错误", true),

    /**
     * 0010400006，leaf id生成，超出最大重试次数错误
     */
    LEAF_ID_GENERATION_MAX_RETRY_ERROR(SECOND_SYS, "00006", "leaf id生成，超出最大重试次数错误", true),

    // ---------------------------- 第三方业务错误，如：0010500001 ----------------------------

    // ---------------------------- 第三方系统错误，如：0010600001 ----------------------------

    ;

    /**
     * 错误类型
     */
    private final TransErrorMiddleTypeEnum middleType;

    /**
     * 具体错误码
     */
    private final String errorCode;

    /**
     * 缺省错误信息
     */
    private final String errorMsg;

    /**
     * 是否可重试，除非显式地指定可重试，否则全部都是不可重试的
     */
    private final boolean retryable;

    /**
     * 通过合成的错误码获取错误枚举
     *
     * @param synthesizedErrorCode 合成的错误码
     * @return 错误枚举
     */
    public static TransErrorEnum getBySynthesizedErrorCode(String synthesizedErrorCode) {
        if (null == synthesizedErrorCode) {
            return null;
        }
        for (TransErrorEnum value : TransErrorEnum.values()) {
            if (value.getSynthesizedErrorCode().equals(synthesizedErrorCode)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取合成的错误码
     *
     * @return 合成的错误码
     */
    public String getSynthesizedErrorCode() {
        return TransSysConfigEnum.TRANS_CORE.getCode() +
                middleType.getCode() +
                errorCode;
    }
}
