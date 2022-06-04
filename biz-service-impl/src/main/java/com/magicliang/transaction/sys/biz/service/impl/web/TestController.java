package com.magicliang.transaction.sys.biz.service.impl.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/res/v1")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
