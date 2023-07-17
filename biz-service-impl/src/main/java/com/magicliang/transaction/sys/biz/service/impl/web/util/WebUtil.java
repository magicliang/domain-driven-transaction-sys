package com.magicliang.transaction.sys.biz.service.impl.web.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.magicliang.transaction.sys.biz.service.impl.web.http.ContentHeaderCachingRequestWrapper;
import com.magicliang.transaction.sys.biz.service.impl.web.model.vo.ApiResult;
import com.magicliang.transaction.sys.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2023-03-30 17:05
 */
@Slf4j
public class WebUtil {

    private WebUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取当前的请求
     *
     * @return 当前的请求
     */
    public static HttpServletRequest getCurrentRequest() {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return requestAttributes.getRequest();
    }

    /**
     * 注意，本方法对 request 通常只能调用一次
     * body 是流式的，消费了就消费了
     *
     * @return 请求的 body
     */
    public static String getCurrentRequestBody() {
        final HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }
        return WebUtil.getBodyString(request);
    }

    /**
     * 获取某个特定的 attribute：
     * 1. 返回值的类型要调用方自己确定
     * 2. 取不到值返回 null
     *
     * @param name attribute 名称
     * @param <T>  attribute 的类型
     * @return attribute 的值
     */
    public static <T> T getCurrentRequestAttribute(final String name) {
        return getCurrentRequestAttribute(name, null);
    }

    /**
     * 获取某个特定的 attribute：
     * 1. 返回值的类型要调用方自己确定
     * 2. 可以指定缺省值
     *
     * @param name         attribute 名称
     * @param defaultValue attribute 的缺省值
     * @param <T>          attribute 的类型
     * @return attribute 的值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getCurrentRequestAttribute(final String name, T defaultValue) {
        final HttpServletRequest currentRequest = getCurrentRequest();
        return getRequestAttribute(name, defaultValue, currentRequest);
    }

    public static <T> T getRequestAttribute(String name, HttpServletRequest currentRequest) {
        return getRequestAttribute(name, null, currentRequest);
    }

    public static <T> T getRequestAttribute(String name, T defaultValue, HttpServletRequest currentRequest) {
        if (currentRequest == null) {
            return defaultValue;
        }
        final T attribute = (T) currentRequest.getAttribute(name);
        if (attribute == null) {
            return defaultValue;
        }
        return attribute;
    }

    /**
     * 读取请求体
     *
     * @param request 原始 http 请求
     * @return 请求的 body
     */
    public static String getBodyString(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return readCachingRequest((ContentCachingRequestWrapper) request);
        }
        // 不要嵌套 ContentCachingRequestWrapper，在嵌套的时候 content 可能读不出来
        return readCachingRequest(new ContentCachingRequestWrapper(request));
    }

    public static boolean hasBodyParam(HttpServletRequest request, String paramName) {
        final JsonNode bodyJson = getBodyJson(request);
        if (null == bodyJson) {
            return false;
        }
        return bodyJson.has(paramName);
    }

    public static <T extends Enum<T>> T getEnumFromRequest(HttpServletRequest request, String paramName,
                                                           Class<T> enumType) {
        final JsonNode bodyJson = getBodyJson(request);
        if (null == bodyJson) {
            return null;
        }
        try {
            return Enum.valueOf(enumType, bodyJson.asText().trim());
        } catch (Exception e) {
            throw new RuntimeException(paramName + "参数错误");
        }
    }

    public static Boolean getBodyParamAsBoolean(HttpServletRequest request, String paramName, boolean required) {
        final Boolean param = getBodyParamAsBoolean(request, paramName);
        if (param == null && required) {
            throw new RuntimeException(paramName + "为必填项");
        }
        return param;
    }

    public static Boolean getBodyParamAsBoolean(HttpServletRequest request, String paramName) {
        final JsonNode bodyJson = getBodyJson(request);
        if (null == bodyJson) {
            return null;
        }
        final JsonNode jsonNode = bodyJson.get(paramName);
        if (null == jsonNode) {
            return null;
        }
        return jsonNode.asBoolean();
    }

    /**
     * 读取请求体的一个字段，可能返回空字符串，如果不允许返回空字符串，抛出异常
     *
     * @param request   原始 http 请求
     * @param paramName 字段名称
     * @param notBlank  是否不允许为空
     * @return 请求的 body 的一个 element
     */
    public static String getBodyParamAsText(HttpServletRequest request, String paramName, boolean notBlank) {
        final String bodyParam = getBodyParamAsText(request, paramName);
        if (notBlank && StringUtils.isBlank(bodyParam)) {
            throw new RuntimeException(paramName + "为空");
        }
        return bodyParam;
    }

    /**
     * 读取请求体的一个字段，可能返回空字符串
     *
     * @param request   原始 http 请求
     * @param paramName 字段名称
     * @return 请求的 body 的一个 element，如果找不到返回空字符串
     */
    public static String getBodyParamAsText(HttpServletRequest request, String paramName) {
        final JsonNode bodyJson = getBodyJson(request);
        if (null == bodyJson) {
            return "";
        }
        final JsonNode jsonNode = bodyJson.get(paramName);
        if (null == jsonNode) {
            return "";
        }
        return jsonNode.asText();
    }

    public static Integer getBodyParamAsInteger(HttpServletRequest request, String paramName, boolean required) {
        final Integer param = getBodyParamAsInteger(request, paramName);
        if (param == null && required) {
            throw new RuntimeException(paramName + "为必填项");
        }
        return param;
    }

    public static Integer getBodyParamAsInteger(HttpServletRequest request, String paramName) {
        final JsonNode bodyJson = getBodyJson(request);
        if (null == bodyJson) {
            return null;
        }
        final JsonNode jsonNode = bodyJson.get(paramName);
        if (null == jsonNode) {
            return null;
        }
        return jsonNode.asInt();
    }

    public static Long getBodyParamAsLong(HttpServletRequest request, String paramName, boolean required) {
        final Long param = getBodyParamAsLong(request, paramName);
        if (param == null && required) {
            throw new RuntimeException(paramName + "为必填项");
        }
        return param;
    }

    public static Long getBodyParamAsLong(HttpServletRequest request, String paramName) {
        final JsonNode bodyJson = getBodyJson(request);
        if (null == bodyJson) {
            return null;
        }
        final JsonNode jsonNode = bodyJson.get(paramName);
        if (null == jsonNode) {
            return null;
        }
        return jsonNode.asLong();
    }

    /**
     * 读取请求体为 JsonNode，为后续搜索查找提供帮助
     *
     * @param request 原始 http 请求
     * @return 请求的 body 的 JsonNode 形态
     */
    public static JsonNode getBodyJson(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        final String bodyString = getBodyString(request);
        if (StringUtils.isBlank(bodyString)) {
            return null;
        }
        return JsonUtils.toJsonNode(bodyString);
    }

    /**
     * 读取当前请求体为 JsonNode，为后续搜索查找提供帮助
     *
     * @return 请求的 body 的 JsonNode 形态
     */
    public static JsonNode getCurrentRequestBodyJson() {
        final HttpServletRequest request = getCurrentRequest();
        return getBodyJson(request);
    }

    public static String getBodyString(ContentCachingResponseWrapper wrappedResponse) {
        try {
            return new String(wrappedResponse.getContentAsByteArray(),
                    wrappedResponse.getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            log.error("getBodyString error, ", e);
            return "";
        }
    }

    public static String getCurrentRequestCookie(String cookieName) {
        return getCookie(getCurrentRequest(), cookieName, "");
    }

    public static String getCurrentRequestCookie(String cookieName,
                                                 String defaultValue) {
        return getCookie(getCurrentRequest(), cookieName, defaultValue);
    }

    public static String getCookie(HttpServletRequest request, String cookieName) {
        return getCookie(request, cookieName, "");
    }

    public static String getCookie(HttpServletRequest request, String cookieName,
                                   String defaultValue) {
        if (request == null) {
            return defaultValue;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return defaultValue;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue() == null ? defaultValue : cookie.getValue();
            }
        }
        return defaultValue;
    }

    /**
     * 获取任意请求查询参数的值
     *
     * @param request        任意请求
     * @param queryParamName 查询参数名称
     * @return 查询参数的值
     */
    public static String getQueryString(final HttpServletRequest request, final String queryParamName) {
        final Object queryParam = getParameterMap(request).get(queryParamName);
        return queryParam != null ? queryParam + "" : "";
    }

    /**
     * 获取当前请求的查询参数的值
     *
     * @param queryParamName 查询参数名称
     * @return 查询参数的值
     */
    public static String getCurrentRequestQueryString(final String queryParamName) {
        return getQueryString(getCurrentRequest(), queryParamName);
    }

    /**
     * 将请求参数转化为 Map
     *
     * @param request 原始请求
     * @return 参数映射表
     */
    public static Map<String, Object> getParameterMap(final HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>(16);
        try {
            Enumeration<String> em = request.getParameterNames();
            while (em.hasMoreElements()) {
                String key = em.nextElement();
                param.put(key, request.getParameter(key));
            }
        } catch (Exception e) {
            log.error("getParameterMap error, ", e);
        }
        return param;
    }

    /**
     * 注意，调用这个方法必须在 AbstractMessageConverterMethodArgumentResolver.readWithMessageConverters 之后
     * 目前尝试激活 tryLoadCacheContent 会破坏底层 ServletInputStream 的完整性，也就导致 @RequestBody 注解的参数生
     * 成失败。所以本方法最好在 filterChain.doFilter(wrappedRequest, wrappedResponse); 之后被调用，在这之前就可能出问题
     *
     * @param request 待读取请求
     * @return 读取出的字符串
     */
    private static String readCachingRequest(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length == 0) {
            // 再赋值一次
            content = tryLoadCacheContent(request);
        }

        if (content.length == 0) {
            return "";
        }
        try {
            return new String(content, request.getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            log.error("getBodyString, ", e);
            return "";
        }
    }

    /**
     * 尝试加载这个内容缓存包装器的内容，如果这个包装器的内容已经被加载过，返回空字节数组。
     * <p>
     * HttpServletRequest 的 body 的本质是一个 InputStream，这个流在设计上只能被一次读取
     * ContentCachingRequestWrapper 的原理是在内部维护了一个 ByteOutputStream 作为
     * content，当输入流每次被读取时，都偷偷转存字节到 content 里，这样形成了一个特定 的结果：
     * 1. InputStream 只能被读取一次，在它被读取以前，content 无内容。
     * 2. 当 content 有内容时 InputStream 已不可读。强制加载 content 会导致
     * InputStream 不可读。
     * 3. 在 Spring 的核心流程里，有些流程会依赖于 InputStream，如 AbstractMessageConverterMethodArgumentResolver，
     * 核心流程隐藏在  filterChain.doFilter(wrappedRequest, wrappedResponse)里，
     * 所以，不要尝试在 doFilter 之前 tryLoadCacheContent，不然会出现奇奇奇怪的问题。
     *
     * @param cac 内容缓存请求包装器
     * @return 读出的内容，格式为字节数组。读取异常或者 cac 已被读取过时，返回空字节数组。
     */
    private static byte[] tryLoadCacheContent(ContentCachingRequestWrapper cac) {
        try {
            // 强制激活这个 wrappedRequest
            return IOUtils.toByteArray(cac.getInputStream());
        } catch (Exception ex) {
            log.error("tryLoadCacheContent, ", ex);
        }
        return new byte[0];
    }

    private static String readNormalRequest(HttpServletRequest request) {
        BufferedReader br;
        try {
            br = request.getReader();
        } catch (IOException e) {
            log.error("getBodyString, ", e);
            // 宁可返回空，不返回 malformed 的 body
            return null;
        }
        String str;
        StringBuilder wholeStr = new StringBuilder();
        while (true) {
            try {
                if ((str = br.readLine()) == null) {
                    break;
                }
            } catch (IOException e) {
                log.error("getBodyString, ", e);
                // 宁可返回空串，不返回 malformed 的 body
                return null;
            }
            wholeStr.append(str);
        }
        return wholeStr.toString();
    }

    public static <T> ResponseEntity<ApiResult<T>> getSuccessResult(T data) {
        return ok(ApiResult.success(data));
    }

    public static <T> ResponseEntity<ApiResult<T>> getSuccessResult() {
        return ok(ApiResult.success());
    }

    public static ResponseEntity<ApiResult<String>> getFailResult(String msg) {
        return ok(ApiResult.fail(msg));
    }

    public static ResponseEntity<ApiResult<String>> getFailResult(String msg, int errorCode) {
        return ok(failResult(msg, errorCode));
    }

    public static <T> ResponseEntity<ApiResult<T>> ok(ApiResult<T> content) {
        return rawOk(content);
    }

    public static <T> ResponseEntity<T> rawOk(T content) {
        return ResponseEntity.ok(content);
    }

    public static ApiResult<String> failResult(String msg) {
        return ApiResult.fail(msg);
    }

    public static ApiResult<String> failResult(String msg, int errorCode) {
        return ApiResult.fail(msg, errorCode);
    }

    public static <T> ApiResult<T> failResult(String msg, T data) {
        return ApiResult.fail(msg, data);
    }

    public static String getHostWithoutScheme(final HttpServletRequest request) {
        return request.getHeader("Host");
    }

    public static String copyAbsoluteURL(final HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (queryString != null) {
            url += "?" + queryString;
        }
        return url;
    }

    public static void addHeader(String key, String value) {
        addHeaderToCurrentHttpRequest(key, value);
    }


    public static void addHeaderToCurrentHttpRequest(String key,
                                                     String value) {
        addHeaderToHttpRequest(getCurrentRequest(), key, value);
    }

    public static boolean addHeaderToHttpRequest(HttpServletRequest request, String key,
                                                 String value) {
        if (request instanceof ContentHeaderCachingRequestWrapper) {
            addHeaderToRequestWrapper((ContentHeaderCachingRequestWrapper) request, key, value);
            return true;
        }
        return false;
    }

    public static void addHeaderToRequestWrapper(ContentHeaderCachingRequestWrapper request, String key, String value) {
        request.addHeader(key, value);
    }


    public static String getPath(HttpServletRequest request) {
        final String contextPath = request.getContextPath();
        final String servletPath = request.getServletPath();
        log.info("contextPath: {}, servletPath: {}", contextPath, servletPath);
        return contextPath + servletPath;
    }

}

