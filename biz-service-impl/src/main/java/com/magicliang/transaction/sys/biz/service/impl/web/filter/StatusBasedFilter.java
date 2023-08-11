package com.magicliang.transaction.sys.biz.service.impl.web.filter;

import com.magicliang.transaction.sys.biz.service.impl.web.util.WebUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 基于错误码的过滤器，等同于原始架构里的 ErrorHandler 的 onClientError
 * 另一种方案是：HandlerInterceptor
 * 另一种方案是：ResponseBodyAdvice
 * <p>
 * 他们都有不能读或者写 status 的问题，如果我们使用 filter，则以后还可以迁移到其他兼容 servlet 的技术栈中，总比使用 Spring 原生 api 好
 *
 * @author magicliang
 *         <p>
 *         date: 2022-11-15 11:31
 */
//@Component
@Slf4j
public class StatusBasedFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 如果要记录 response，使用 ContentCachingResponseWrapper，参考这篇文章：https://cloud.tencent.com/developer/article/1825102
        // 包装HttpServletRequest，把输入流缓存下来
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(
                request);
        // 包装HttpServletResponse，把输出流缓存下来
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(
                response);

        filterChain.doFilter(request, wrappedResponse);

        String requestBody = WebUtil.getBodyString(wrappedRequest);
        String responseBody = WebUtil.getBodyString(wrappedResponse);
        log.info("requestBody: " + requestBody);
        log.info("responseBody: " + responseBody);

        /*
         * 要输出原始的响应则可以使用这样的方法：
         * response.setContentLength(responseWrapper.getContentSize());
         * response.getOutputStream().write(responseWrapper.getContentAsByteArray());
         * 消费 body 的几种方法：
         *  String responseBody = IOUtils.toString(wrappedResponse.getContentInputStream(), UTF_8);
         *  String body = new String(wrappedResponse.getContentAsByteArray(), wrappedResponse.getCharacterEncoding());
         * 另一种重写方法是：HttpServletResponseWrapper
         * 比如：https://blog.csdn.net/u011410529/article/details/78873404
         */

        int status = wrappedResponse.getStatus();
        // 在这里，如果读了 status，如果消耗掉了对外输出流，要改写输出流就直接改写，不能改写输出流则需要使用 copyBodyToResponse 还原响应
        if (isClientError(status)) {
            onClientError(response);
        } else {
            wrappedResponse.copyBodyToResponse();
        }

    }


    /**
     * 当触发客户端错误时，重写错误信息
     *
     * @param response 原始响应
     * @throws IOException 可能抛出的 IO 异常，交给 filter 机制处理
     */
    private void onClientError(HttpServletResponse response) throws IOException {
        final int statusCode = response.getStatus();
        final Collection<String> headerNames = response.getHeaderNames();
        Map<String, Collection<String>> headerMap = recordHeaders(response, headerNames);
        // 会清空 status、headers 和 body
        response.reset();
        // 所以我们要重建 header
        resetHeaders(response, headerMap);
        // 这个顺序极为重要！，要在写入新的内容以前就设置好 status，不然 status 会变 200
        response.setStatus(statusCode);
        final String errorMsg = "A client error occurred, statusCode: " + statusCode;

        final PrintWriter writer = response.getWriter();
        writer.print(errorMsg);
        response.setContentLength(errorMsg.length());
        writer.flush();
    }

    /**
     * 记录响应头
     *
     * @param response 原始的 HttpServlet 响应
     * @param headerNames 响应头的名称
     * @return 被记录的响应头
     */
    private static Map<String, Collection<String>> recordHeaders(HttpServletResponse response,
            Collection<String> headerNames) {
        Map<String, Collection<String>> headerMap = new HashMap<>();
        for (String headerName : headerNames) {
            final Collection<String> headers = response.getHeaders(headerName);
            headerMap.put(headerName, headers);
        }
        return headerMap;
    }

    /**
     * 重建响应头
     *
     * @param response 重建响应头
     * @param headerMap 响应头记录映射表
     */
    private static void resetHeaders(HttpServletResponse response, Map<String, Collection<String>> headerMap) {
        for (Entry<String, Collection<String>> entry : headerMap.entrySet()) {
            final String headerName = entry.getKey();
            final Collection<String> headers = entry.getValue();
            for (String header : headers) {
                response.addHeader(headerName, header);
            }
        }
    }

    /**
     * 确认一个响应的错误码是不是客户端错误
     * 具体算法服从 play 和 eclipse 的思路
     *
     * @param code 响应错误码。Must be greater or equal to 400, and less than 500，然后返回 true，否则返回 false。
     * @return 一个响应的错误码是不是客户端错误
     */
    private static boolean isClientError(int code) {
        return ((400 <= code) && (code <= 499));
    }

}
