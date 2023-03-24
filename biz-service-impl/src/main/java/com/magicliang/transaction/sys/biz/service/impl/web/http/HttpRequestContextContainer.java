package com.magicliang.transaction.sys.biz.service.impl.web.http;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 带缓存的请求包装器，支持在拷贝模式下对请求内容进行各种消费
 * 这也会更好：
 * ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
 * .getRequestAttributes();
 * HttpServletRequest request = requestAttributes.getRequest();
 *
 * @author magicliang
 * <p>
 * date: 2023-03-24 10:59
 */
@Getter
@Slf4j
public class HttpRequestContextContainer {

    private final static ThreadLocal<HttpRequestContextContainer> contextContainer =
            new ThreadLocal<HttpRequestContextContainer>();

    /**
     * 包装HttpServletRequest，把输入流缓存下来
     */
    private ContentCachingRequestWrapper wrappedRequest;

    /**
     * 包装HttpServletResponse，把输出流缓存下来
     */
    private ContentCachingResponseWrapper wrappedResponse;

    /**
     * 缓存过的请求头，可能在一个 request scope 里被修改
     */
    private MultiValueMap<String, String> cachedHeaders;

    /**
     * 请求参数，包括 query params 和 form data
     */
    private Map<String, Object> cachedRequestParameters;

    /**
     * 解析过的请求体，可能为 raw json
     */
    private String cachedRequestBody;

    public static HttpRequestContextContainer begin(final HttpServletRequest request,
                                                    final HttpServletResponse response) {
        return new HttpRequestContextContainer(request, response);
    }

    public static HttpRequestContextContainer get() {
        return contextContainer.get();
    }

    public HttpRequestContextContainer(final HttpServletRequest request, final HttpServletResponse response) {
        wrappedRequest = new ContentCachingRequestWrapper(request);
        wrappedResponse = new ContentCachingResponseWrapper(response);

        initCachedHeaders();
        initParameters();
        initRequestBody();
        contextContainer.set(this);
    }

    /**
     * 销毁
     */
    public void end() {
        try {
            wrappedResponse.copyBodyToResponse();
        } catch (IOException e) {
            log.error("wrappedResponse.copyBodyToResponse error, ", e);
        }
        this.wrappedRequest = null;
        this.wrappedResponse = null;
        contextContainer.remove();
    }

    /**
     * 动态获取 cookie 的值
     * 如果性能不好，考虑缓存这些值
     *
     * @param cookieName cookie 的名称
     * @return cookie 的值
     */
    public String getCookie(final String cookieName) {
        final Cookie[] cookies = wrappedRequest.getCookies();
        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), cookieName)) {
                return cookie.getValue();
            }
        }
        return "";
    }

    /**
     * 增加 header
     *
     * @param key   key
     * @param value value
     */
    public void addHeader(final String key, final String value) {
        getCachedHeaders().add(key, value);
    }

    /**
     * 获取特定的 header 的值
     *
     * @param key header 的 key
     * @return header 的值列表
     */
    public List<String> getHeaders(final String key) {
        return getCachedHeaders().get(key);
    }

    /**
     * 获取查询参数的值
     *
     * @param queryParamName 查询参数名称
     * @return 查询参数的值
     */
    public String getQueryString(String queryParamName) {
        final Object queryParam = getCachedRequestParameters().get(queryParamName);
        return queryParam != null ? queryParam + "" : "";
    }

    /**
     * 获取特定的 header 的值
     *
     * @param key header 的 key
     * @return header 的值
     */
    public String getHeader(final String key) {
        final List<String> headers = getHeaders(key);
        return CollectionUtils.isNotEmpty(headers) ? headers.get(0) : "";
    }

    /**
     * 初始化缓存过的请求头
     */
    private void initCachedHeaders() {
        final Enumeration<String> headerNames = wrappedRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            final Enumeration<String> headers = wrappedRequest.getHeaders(headerName);
            final ArrayList<String> headerList = Collections.list(headers);
            cachedHeaders.put(headerName, headerList);
        }
    }

    /**
     * 初始化请求参数列表
     */
    private void initParameters() {
        cachedRequestParameters = getParameterMap(wrappedRequest);
    }

    /**
     * 初始化请求体
     */
    private void initRequestBody() {
        try {
            cachedRequestBody = new String(wrappedRequest.getContentAsByteArray(),
                    wrappedRequest.getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            log.error("wrappedResponse parse content to body error, ", e);
        }
    }

    /**
     * 将请求参数转化为 Map
     *
     * @param request 原始请求
     * @return 参数映射表
     */
    private Map<String, Object> getParameterMap(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>(16);
        try {
            Enumeration<String> em = request.getParameterNames();
            while (em.hasMoreElements()) {
                String key = em.nextElement();
                param.put(key, request.getParameter(key));
            }
        } catch (Exception e) {
            log.error("wrappedResponse getParameterMap error, ", e);
        }
        return param;
    }

}
