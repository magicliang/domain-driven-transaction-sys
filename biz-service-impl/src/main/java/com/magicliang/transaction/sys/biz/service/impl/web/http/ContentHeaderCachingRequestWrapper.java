package com.magicliang.transaction.sys.biz.service.impl.web.http;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2023-06-21 14:56
 */
public class ContentHeaderCachingRequestWrapper extends ContentCachingRequestWrapper {

    /**
     * construct a wrapper for this request
     *
     * @param request
     */
    public ContentHeaderCachingRequestWrapper(HttpServletRequest request) {
        super(request);
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            final Enumeration<String> headers = request.getHeaders(headerName);
            final ArrayList<String> headerList = Collections.list(headers);
            headerMap.putAll(headerName, headerList);
        }
    }

    private ListMultimap<String, String> headerMap = LinkedListMultimap.create();

    /**
     * add a header with given name and value
     *
     * @param name
     * @param value
     */
    public void addHeader(String name, String value) {
        headerMap.put(name, value);
    }

    /**
     * add a header with given name and value
     *
     * @param name
     * @param values
     */
    public void addHeaders(String name, String... values) {
        headerMap.putAll(name, Arrays.asList(values));
    }

    @Override
    public String getHeader(String name) {
        String headerValue = super.getHeader(name);
        if (StringUtils.isNotBlank(headerValue)) {
            return headerValue;
        }
        if (!headerMap.containsKey(name)) {
            return "";

        }

        List<String> headerValues = headerMap.get(name);
        if (CollectionUtils.isEmpty(headerValues)) {
            return "";
        }

        return headerValues.get(0);
    }

    /**
     * get the Header names
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        for (String name : headerMap.keySet()) {
            names.add(name);
        }
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> values = Collections.list(super.getHeaders(name));
        if (headerMap.containsKey(name)) {
            values.addAll(headerMap.get(name));
        }
        return Collections.enumeration(values);
    }

}
