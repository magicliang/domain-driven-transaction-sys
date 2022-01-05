package com.magicliang.transaction.sys.biz.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 程序主入口类
 */
@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableTransactionManagement
@PropertySource(value = {
		"classpath:META-INF/conf/application.properties"
})
@ImportResource("classpath:/applicationContext.xml")
public class DomainDrivenTransactionSysApplication {

	/**
	 * 程序主入口
	 *
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		SpringApplication.run(DomainDrivenTransactionSysApplication.class, args);
		// 让主日志可以得到程序启动的标志点
		log.info("服务启动成功！");
	}

}
