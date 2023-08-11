package com.magicliang.transaction.sys.biz.service.impl.web.controller;

import com.magicliang.transaction.sys.biz.service.impl.web.util.WebUtil;
import com.magicliang.transaction.sys.common.util.JsonUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 测试控制器
 *
 * @author magicliang
 *         <p>
 *         date: 2022-06-04 18:00
 */
@Slf4j
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
    public String hello(HttpServletRequest request) {
        final String path = WebUtil.getPath(request);
        log.info("path: {}", path);
        return "hello, path: " + path;
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
    public Map<String, String> json(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        map.put("1", "a");
        map.put("b", "2");

        // Building cookies
        ResponseCookie cookie = ResponseCookie.from("myCookie", "myCookieValue") // key & value
                .httpOnly(true)
                .secure(false)
                .domain("localhost")  // host
                //    .path("/")      // path
                .maxAge(Duration.ofHours(1))
                .sameSite("none")  // sameSite
                .build();

        // Response to the client
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return map;
    }

    @GetMapping("/async")
    public CompletionStage<ResponseEntity<String>> asyncCompletionStage(HttpServletResponse response) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok("123456"));
    }


    @GetMapping("/redirect1")
    public RedirectView redirect1(RedirectAttributes attributes) {
        return new RedirectView("https://www.baidu.com");
    }


    @GetMapping("/redirect2")
    public String redirect2(RedirectAttributes attributes) {
        // not working
        return "redirect:" + "https://www.baidu.com";
    }

    /**
     * it's working
     *
     * @param attributes 属性
     * @param type 请求类型
     * @return 任意返回值
     */
    @GetMapping("/multiple-response")
    public Object multiResponseType(RedirectAttributes attributes, Integer type) {
        if (type == 1) {
            return new RedirectView("https://www.baidu.com");
        } else if (type == 2) {
            return ResponseEntity.ok("2");
        }
        return "hello";
    }

    @GetMapping("/redirect3")
    public ResponseEntity<Void> redirect3(HttpServletResponse response) {
        Cookie cookie1 = new Cookie("name", "123");
        cookie1.setDomain("www.baidu.com");
        cookie1.setPath("/");
        cookie1.setMaxAge(3600);

        Cookie cookie2 = new Cookie("foo", "bar");
        cookie2.setDomain("www.baidu.com");
        cookie2.setPath("/");
        cookie2.setMaxAge(996);
        response.addCookie(cookie1);
        response.addCookie(cookie2);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://www.baidu.com"));
        return new ResponseEntity<Void>(headers, HttpStatus.FOUND);
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
