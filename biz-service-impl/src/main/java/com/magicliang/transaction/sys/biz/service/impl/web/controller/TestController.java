package com.magicliang.transaction.sys.biz.service.impl.web.controller;

import com.magicliang.transaction.sys.common.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 测试控制器
 *
 * @author magicliang
 * <p>
 * date: 2022-06-04 18:00
 */
@RestController
@RequestMapping("/res/v1/test")
public class TestController {

    @Autowired
    private ResourceLoader resourceLoader;

    final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";

    /**
     * 1. soa swagger api 注解
     * 2. 注释
     * 3. 约束
     * 4. gson 定义反序列化格式
     *
     * @return 健康信息
     */
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    /**
     * 1. soa swagger api 注解
     * 2. 注释
     * 3. 约束
     * 4. gson 定义反序列化格式
     *
     * @return 健康信息
     */
    @GetMapping("/json")
    @ResponseBody
    public Map<String, String> json() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "a");
        map.put("b", "2");
        return map;
    }

    @GetMapping("/downloadmultimedia")
    public ResponseEntity<StreamingResponseBody> getMediaData() throws UnsupportedEncodingException {
        try {
            byte[] mediaData = getBytesFromStaticFile();

            Map<String, String> headerMap = new HashMap<>();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(mediaData);

            StreamingResponseBody stream = outputStream -> {
                byte[] data = new byte[1024 * 8];
                int bytesRead;
                while ((bytesRead = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, bytesRead);
                    outputStream.flush();
                }
                inputStream.close();
            };

//            headerMap.put(HttpHeaders.CONTENT_TYPE, "video/mp4");
//            headerMap.put(HttpHeaders.TRANSFER_ENCODING, "chunked");
            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAll(headerMap);
//            return new ResponseEntity<StreamingResponseBody>
//                    (stream, httpHeaders, HttpStatus.OK);

            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                    .header(HttpHeaders.TRANSFER_ENCODING, "chunked")
                    .body(stream);
        } catch (IOException e) {
            Map<String, String> map = new HashMap<>();
            map.put("code", "200");
            map.put("msg", "fail");
            map.put("data", "下载失败，请稍后重试");

            return asStreamingResponseJsonBody(map);
        }
    }

    public byte[] getBytesFromStaticFile() throws IOException {
        // ResourceUtils.getFile("classpath:static/a.mp4");
        Resource resource = resourceLoader.getResource("classpath:static/a.mp4");
        InputStream inputStream = resource.getInputStream();
        // 在 java 9 中，可以使用  inputStream.readAllBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }

    protected ResponseEntity<StreamingResponseBody> asStreamingResponseBody(String toJson, String contentType)
            throws UnsupportedEncodingException {
        byte[] bytes = toJson.getBytes("UTF-8");
        if (StringUtils.isBlank(contentType)) {
            contentType = DEFAULT_CONTENT_TYPE;
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(HttpHeaders.CONTENT_LENGTH, bytes.length + "")
                .body(outputStream -> {
                    outputStream.write(bytes);
                    outputStream.flush();
                });
    }

    protected <T> ResponseEntity<StreamingResponseBody> asStreamingResponseJsonBody(T body)
            throws UnsupportedEncodingException {
        return asStreamingResponseBody(JsonUtils.toJson(body), "application/json");
    }
}
