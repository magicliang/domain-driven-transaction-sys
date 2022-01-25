package com.magicliang.transaction.sys;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PreDestroy;

/**
 * 程序主入口类
 * 如果没有 exclude = DataSourceAutoConfiguration.class，则会报错：
 * ***************************
 * APPLICATION FAILED TO START
 * ***************************
 * <p>
 * Description:
 * <p>
 * Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.
 * <p>
 * Reason: Failed to determine a suitable driver class
 * <p>
 * <p>
 * Action:
 * <p>
 * Consider the following:
 * If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
 * If you have database settings to be loaded from a particular profile you may need to activate it (the profiles test are currently active).
 * <p>
 * <p>
 * Process finished with exit code 0
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

	/**
	 * Try to test connectivity before bootstrapping completion. Originally we throw exception to stop tomcat here.
	 */
	@Component
	@Order(value = 1)
	static class ConnectionInitializer implements CommandLineRunner {

		@Override
		public void run(String... args) throws Exception {
			log.info(">>>>>>>>>>>>>>> Service is up, beginning to initialize datastore connection <<<<<<<<<<<<<");
		}

	}

	/**
	 * Only if Order 0/1 phases are passed, we will initialize our Executors.
	 */
	@Component
	@Order(value = 2)
	static class ExecutorsManager implements CommandLineRunner {

		@Override
		public void run(String... args) throws Exception {
			log.info("init taskCenter");
		}

		/**
		 * destroy all resources.
		 * This method will be invoked in Spring shutdown hook while gracefully shutting down.
		 */
		@PreDestroy
		private void destroy() {
			log.info("destroy taskCenter");
		}
	}

}
