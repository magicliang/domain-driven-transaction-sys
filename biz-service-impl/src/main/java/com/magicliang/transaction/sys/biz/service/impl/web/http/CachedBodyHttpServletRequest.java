package com.magicliang.transaction.sys.biz.service.impl.web.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * project name: domain-driven-transaction-sys
 *
 * description:
 *
 * @author magicliang
 *
 *         date: 2024-10-12 15:08
 */
public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

    private byte[] cachedBody;

    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        ByteArrayOutputStream cachedBytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = requestInputStream.read(buffer)) != -1) {
            cachedBytes.write(buffer, 0, bytesRead);
        }
        cachedBody = cachedBytes.toByteArray();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CachedBodyServletInputStream(cachedBody);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
    }

    private static class CachedBodyServletInputStream extends ServletInputStream {

        private ByteArrayInputStream cachedBytesInputStream;

        public CachedBodyServletInputStream(byte[] cachedBody) {
            this.cachedBytesInputStream = new ByteArrayInputStream(cachedBody);
        }

        @Override
        public int read() throws IOException {
            return cachedBytesInputStream.read();
        }

        @Override
        public boolean isFinished() {
            return cachedBytesInputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException("Not implemented");
        }
    }
}