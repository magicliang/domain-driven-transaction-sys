package com.magicliang.transaction.sys.biz.service.impl.web.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
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

    public static String getBodyString(ContentCachingResponseWrapper wrappedResponse) {
        try {
            return new String(wrappedResponse.getContentAsByteArray(),
                    wrappedResponse.getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            log.error("getBodyString error, ", e);
            return "";
        }
    }

    public static String getCookie(HttpServletRequest request, String cookieName) {
        return getCookie(request, cookieName, "");
    }

    public static String getCookie(HttpServletRequest request, String cookieName,
                                   String defaultValue) {
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
     * 获取查询参数的值
     *
     * @param queryParamName 查询参数名称
     * @return 查询参数的值
     */
    public static String getQueryString(final HttpServletRequest request, final String queryParamName) {
        final Object queryParam = getParameterMap(request).get(queryParamName);
        return queryParam != null ? queryParam + "" : "";
    }

    /**
     * 将请求参数转化为 Map
     *
     * @param request 原始请求
     * @return 参数映射表
     */
    private static Map<String, Object> getParameterMap(final HttpServletRequest request) {
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
        BufferedReader br = null;
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

}

