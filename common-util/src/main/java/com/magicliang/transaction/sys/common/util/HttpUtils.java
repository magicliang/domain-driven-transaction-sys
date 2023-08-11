package com.magicliang.transaction.sys.common.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: http 工具类
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-30 13:41
 */
@Slf4j
public class HttpUtils {


    /**
     * 连接超时时间60s
     */
    public static final int CONNECTION_TIMEOUT_MS = 60000;
    /**
     * 读取数据超时时间 60s
     */
    public static final int SO_TIMEOUT_MS = 60000;
    /**
     * httpclient读取内容时使用的字符集
     */
    public static final String CONTENT_CHARSET = "utf-8";
    public static final String CONTENT_CHARSET_GBK = "gbk";
    public static final String CONTENT_TYPE_JSON_CHARSET = "application/json;charset=" + CONTENT_CHARSET;
    public static final Charset UTF_8 = Charset.forName(CONTENT_CHARSET);
    public static final Charset GBK = Charset.forName(CONTENT_CHARSET_GBK);

    /**
     * 私有构造器
     */
    private HttpUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 简单get调用
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String simpleGetInvoke(String url, Map<String, String> params)
            throws IOException, URISyntaxException {
        return simpleGetInvoke(url, params, CONTENT_CHARSET);
    }

    /**
     * 简单get调用
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String simpleGetInvoke(String url, Map<String, String> params, String charset)
            throws IOException, URISyntaxException {
        return simpleHttpInvoke(charset, buildHttpGet(url, params), null);
    }

    /**
     * get 获取字节数组
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static byte[] simpleGetInvoke(String url) throws IOException {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        byte[] returnByte = null;
        HttpGet method = new HttpGet(url);
        try {
            client = buildHttpClient(false);
            response = client.execute(method);
            try {
                assertStatus(response);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    returnByte = EntityUtils.toByteArray(entity);
                }
            } finally {
                closeCloseable(response);
            }
        } finally {
            closeCloseable(client);
        }
        return returnByte;
    }

    /**
     * 简单post调用
     *
     * @param url
     * @param params
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public static String simplePostInvoke(String url, Map<String, String> params) throws Exception {
        return simplePostInvoke(url, params, CONTENT_CHARSET);
    }

    /**
     * 简单post调用
     *
     * @param url
     * @param params
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public static String simplePostInvoke(String url, Map<String, String> params, String charset)
            throws URISyntaxException, IOException {
        return simpleHttpInvoke(charset, buildHttpPost(url, params), null);
    }

    /**
     * 简单http调用
     *
     * @param charset 字符集
     * @param method httpMethod
     * @param config 配置特殊配置，比如超时时间
     * @return
     * @throws IOException
     */
    public static String simpleHttpInvoke(String charset, HttpRequestBase method, RequestConfig config)
            throws IOException {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        String returnStr = null;
        if (null != method && null != config) {
            method.setConfig(config);
        }
        try {
            client = buildHttpClient(false);
            response = client.execute(method);
            try {
                assertStatus(response);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    returnStr = EntityUtils.toString(entity, charset);
                }
            } finally {
                closeCloseable(response);
            }
        } finally {
            closeCloseable(client);
        }
        return returnStr;
    }

    /**
     * 创建HttpClient
     *
     * @param isMultiThread
     * @return
     */
    public static CloseableHttpClient buildHttpClient(boolean isMultiThread) {

        CloseableHttpClient client;

        if (isMultiThread) {
            client = HttpClientBuilder.create().setConnectionManager(new PoolingHttpClientConnectionManager()).build();
        } else {
            client = HttpClientBuilder.create().build();
        }
        return client;
    }

    /**
     * 创建HttpClient
     *
     * @param isMultiThread
     * @return
     */
    public static CloseableHttpClient buildHttpsClient(boolean isMultiThread, HttpHost proxy, String username,
            String password) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        if (isMultiThread) {
            httpClientBuilder.setConnectionManager(new PoolingHttpClientConnectionManager());
        }
        if (null != username) {
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            //甚至代理用户名密码
            credsProvider.setCredentials(
                    new AuthScope(proxy.getHostName(), proxy.getPort()),
                    new UsernamePasswordCredentials(username, password));
            httpClientBuilder.setDefaultCredentialsProvider(credsProvider);
        }
        //设置证书信任所有
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustStrategy() {
                // 信任所有
                @Override
                public boolean isTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            httpClientBuilder.setSSLSocketFactory(sslsf);
        } catch (Exception e) {
            log.error("构建httpclient错误", e);
            throw new RuntimeException("构建httpclient错误", e);
        }
        return httpClientBuilder.build();
    }

    /**
     * 构建httpPost对象
     *
     * @param url
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     * @throws URISyntaxException
     */
    public static HttpPost buildHttpPost(String url, Map<String, String> params)
            throws UnsupportedEncodingException, URISyntaxException {
        return buildHttpPost(url, params, UTF_8);

    }

    /**
     * 构建httpPost对象
     *
     * @param url
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     * @throws URISyntaxException
     */
    public static HttpPost buildHttpPost(String url, Map<String, String> params, Charset charset)
            throws UnsupportedEncodingException, URISyntaxException {
        Validate.notNull(url, "构建HttpPost时,url不能为null");
        HttpPost post = new HttpPost(url);
        setCommonHttpMethod(post);
        HttpEntity he = null;
        if (params != null) {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                formparams.add(new BasicNameValuePair(key, params.get(key)));
            }
            he = new UrlEncodedFormEntity(formparams, charset);
            post.setEntity(he);
        }
        return post;
    }

    /**
     * 构建httpPost对象
     *
     * @param url
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     * @throws URISyntaxException
     */
    public static HttpPost buildHttpPost(String url, String data, Charset charset)
            throws UnsupportedEncodingException, URISyntaxException {
        Validate.notNull(url, "构建HttpPost时,url不能为null");
        HttpPost post = new HttpPost(url);
        setCommonHttpMethod(post);
        HttpEntity he = new StringEntity(data, charset);
        post.setEntity(he);
        return post;
    }

    /**
     * 构建httpGet对象
     *
     * @param url
     * @param params
     * @return
     * @throws URISyntaxException
     */
    public static HttpGet buildHttpGet(String url, Map<String, String> params) throws URISyntaxException {
        Validate.notNull(url, "构建HttpGet时,url不能为null");
        HttpGet get = new HttpGet(buildGetUrl(url, params));
        setCommonHttpMethod(get);
        return get;
    }

    /**
     * build getUrl str
     *
     * @param url
     * @param params
     * @return
     */
    public static String buildGetUrl(String url, Map<String, String> params) {
        StringBuilder uriStr = new StringBuilder(url);
        if (params != null) {
            List<NameValuePair> ps = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                ps.add(new BasicNameValuePair(key, params.get(key)));
            }
            if (uriStr.indexOf("?") <= -1) {
                uriStr.append("?");
            } else {
                uriStr.append("&");
            }
            uriStr.append(URLEncodedUtils.format(ps, UTF_8));
        }
        return uriStr.toString();
    }

    /**
     * 设置HttpMethod通用配置
     *
     * @param httpMethod
     */
    public static void setCommonHttpMethod(HttpRequestBase httpMethod) {
        httpMethod.setHeader(HTTP.CONTENT_ENCODING, CONTENT_CHARSET);// setting
        httpMethod.setConfig(buildRequestConfig());
    }

    /**
     * 设置成消息体的长度 setting MessageBody length
     *
     * @param httpMethod
     * @param he
     */
    public static void setContentLength(HttpRequestBase httpMethod, HttpEntity he) {
        if (he == null) {
            return;
        }
        httpMethod.setHeader(HTTP.CONTENT_LEN, String.valueOf(he.getContentLength()));
    }

    /**
     * 构建公用RequestConfig
     *
     * @return
     */
    public static RequestConfig buildRequestConfig() {
        return buildRequestConfig(SO_TIMEOUT_MS, CONNECTION_TIMEOUT_MS);
    }

    /**
     * 构建公用RequestConfig
     *
     * @param stm 读取数据超时时间
     * @param ctm 连接超时时间
     * @return
     */
    public static RequestConfig buildRequestConfig(int stm, int ctm) {
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(stm).setConnectTimeout(ctm).build();
        return requestConfig;
    }

    /**
     * 强验证必须是200状态否则报异常
     *
     * @param res
     */
    public static void assertStatus(HttpResponse res) throws IOException {
        Validate.notNull(res, "http响应对象为null");
        Validate.notNull(res.getStatusLine(), "http响应对象的状态为null");
        switch (res.getStatusLine().getStatusCode()) {
            case HttpStatus.SC_OK:
                break;
            default:
                throw new IOException("服务器响应状态异常,失败.status:" + res.getStatusLine().getStatusCode());
        }
    }

    private static void closeCloseable(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            log.error("关闭异常", e);
        }
    }

    /**
     * post json 测试函数
     *
     * @param parameters
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static String postData(HttpEntityEnclosingRequestBase method, String parameters, String charSet)
            throws ParseException, IOException {
        return postData(method, parameters, charSet, CONTENT_TYPE_JSON_CHARSET);
    }

    /**
     * post json 测试函数
     *
     * @param parameters
     * @return
     * @throws IOException
     */
    public static String postData(HttpEntityEnclosingRequestBase method, String parameters, String charSet,
            String inContentType) throws ParseException, IOException {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        String body = null;
        if (StringUtils.isBlank(parameters)) {
            parameters = "{}";
        }
        try {
            client = buildHttpClient(false);
            // 添加参数
            StringEntity entity = new StringEntity(parameters, charSet);
            entity.setContentEncoding(charSet);
            String contentType = CONTENT_TYPE_JSON_CHARSET;
            if (StringUtils.isNotEmpty(inContentType)) {
                contentType = inContentType;
            }
            entity.setContentType(contentType);
            method.setEntity(entity);
            // 设置编码
            response = client.execute(method);
            int statusCode = response.getStatusLine().getStatusCode();
            body = EntityUtils.toString(response.getEntity(), charSet);
            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException("http响应不是200!响应报文:" + body);
            }
        } finally {
            closeCloseable(client);
            closeCloseable(response);
        }
        return body;
    }

    /**
     * post json 测试函数
     *
     * @param parameters
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static String postHttpsData(HttpEntityEnclosingRequestBase method, String parameters, String charSet,
            String inContentType) throws ParseException, IOException {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        String body = null;
        if (StringUtils.isBlank(parameters)) {
            parameters = "{}";
        }
        try {
            client = buildHttpsClient(false, null, null, null);
            // 添加参数
            StringEntity entity = new StringEntity(parameters, charSet);
            entity.setContentEncoding(charSet);
            String contentType = CONTENT_TYPE_JSON_CHARSET;
            if (StringUtils.isNotEmpty(inContentType)) {
                contentType = inContentType;
            }
            entity.setContentType(contentType);
            method.setEntity(entity);
            // 设置编码
            response = client.execute(method);
            int statusCode = response.getStatusLine().getStatusCode();
            body = EntityUtils.toString(response.getEntity(), charSet);
            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException("http响应不是200!响应报文:" + body);
            }
        } finally {
            closeCloseable(client);
            closeCloseable(response);
        }
        return body;
    }
}
