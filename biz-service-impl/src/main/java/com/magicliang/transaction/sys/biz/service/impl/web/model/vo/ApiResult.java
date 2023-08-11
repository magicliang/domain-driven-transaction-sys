package com.magicliang.transaction.sys.biz.service.impl.web.model.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 响应对象
 *
 * @author magicliang
 *         <p>
 *         date: 2023-06-21 14:53
 */
@Data
public class ApiResult<T> implements Serializable {

    /**
     * 响应码
     */
    private int code;

    /**
     * 响应消息
     */


    private String msg = "success";

    /**
     * 响应数据
     */
    private T data;

    public static ApiResult<String> fail() {
        ApiResult<String> apiResult = new ApiResult<>();
        apiResult.setCode(-1);
        apiResult.setMsg("fail");
        return apiResult;
    }

    public static ApiResult<String> fail(String msg) {
        ApiResult<String> apiResult = new ApiResult<>();
        apiResult.setCode(-1);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public static ApiResult<String> fail(String msg, int errorCode) {
        ApiResult<String> apiResult = new ApiResult<>();
        apiResult.setCode(errorCode);
        apiResult.setMsg(msg);
        return apiResult;
    }


    public static <T> ApiResult<T> fail(String msg, T data) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setCode(-1);
        apiResult.setMsg(msg);
        apiResult.setData(data);
        return apiResult;
    }

    public static ApiResult<String> fail(RuntimeException systemException) {
        ApiResult<String> apiResult = new ApiResult<>();
        apiResult.setCode(-1);
        apiResult.setMsg(systemException.getMessage());
        return apiResult;
    }

    public static <T> ApiResult<T> success() {
        return new ApiResult<>();
    }

    public static <T> ApiResult<T> success(T data) {
        return success(null, data);
    }

    public static <T> ApiResult<T> success(String msg, T data) {
        ApiResult<T> result = new ApiResult<>();
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

}