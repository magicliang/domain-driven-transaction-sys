package com.magicliang.transaction.sys.biz.service.impl.web.http;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 带缓存的请求包装器，支持在拷贝模式下对请求内容进行各种消费
 *
 * @author magicliang
 * <p>
 * date: 2023-03-21 20:48
 */
public class HttpRequestWrapper extends HttpServletRequestWrapper {
    private final String body;

    public HttpRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = IOUtils.toString(request.getReader());
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getBody().getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }

        };
        return servletInputStream;
    }

    public String getBody() {
        return this.body;
    }
}
