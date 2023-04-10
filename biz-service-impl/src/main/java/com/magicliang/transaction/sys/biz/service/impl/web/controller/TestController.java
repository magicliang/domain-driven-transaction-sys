package com.magicliang.transaction.sys.biz.service.impl.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @GetMapping("/downloadmultimedia")
    public ResponseEntity<StreamingResponseBody> getMediaData() {
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

            headerMap.put(HttpHeaders.CONTENT_TYPE, "video/mp4");
            headerMap.put(HttpHeaders.TRANSFER_ENCODING, "chunked");
            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAll(headerMap);
            return new ResponseEntity<StreamingResponseBody>
                    (stream, httpHeaders, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.ok(null);
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
}
