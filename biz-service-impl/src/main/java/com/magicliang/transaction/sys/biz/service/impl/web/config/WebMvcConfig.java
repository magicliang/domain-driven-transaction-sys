package com.magicliang.transaction.sys.biz.service.impl.web.config;

import com.magicliang.transaction.sys.biz.service.impl.web.interceptor.InnerApiAuthenticator;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * project name: domain-driven-transaction-sys
 *
 * description:
 *
 * @author magicliang
 *
 *         date: 2023-11-16 22:22
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private InnerApiAuthenticator innerApiAuthenticator;

    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 配置监听器
     * 过滤掉swagger相关请求
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 网关校验拦截器
        registry.addInterceptor(innerApiAuthenticator).addPathPatterns("/**")
                .excludePathPatterns("/v2/api-docs", "/configuration/**", "/swagger-resources/**", "/swagger-ui.html",
                        "/webjars/**");
    }

    /**
     * 配置异步请求处理
     *
     * @param configurer
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(taskExecutor);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
//        messageConverters.add(0, new GsonHttpMessageConverter(new GsonBuilder().setExclusionStrategies(new
//        ExclusionStrategy() {
//                    @Override
//                    public boolean shouldSkipField(FieldAttributes f) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean shouldSkipClass(Class<?> incomingClass) {
//                        return false;
//                    }
//                }).setDateFormat("yyyy-MM-dd HH:mm:ss")
//                .create()));
    }

}
