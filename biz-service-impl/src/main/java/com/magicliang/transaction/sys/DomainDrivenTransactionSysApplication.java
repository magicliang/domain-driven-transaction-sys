package com.magicliang.transaction.sys;

import com.magicliang.transaction.sys.common.dal.mybatis.mapper.TransPayOrderPoMapper;
import com.magicliang.transaction.sys.core.manager.PayOrderManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Arrays;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 程序主入口类
 * 如果没有 exclude = DataSourceAutoConfiguration.class，则会报错：
 * <p>
 * 使用 h2 这类数据源，如果使用自动配置数据源需要使用 DataSourceAutoConfiguration，使用 xml 或者 DataSourceConfig 则需要 exclude =
 * DataSourceAutoConfiguration.class
 * <p>
 * 如果企图使用自动配置数据源，而自动配置数据源不完备，则会报以下错：
 * <p>
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
 * Action:
 * <p>
 * Consider the following:
 * If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
 * If you have database settings to be loaded from a particular profile you may need to activate it (the profiles test
 * are currently active).
 * <p>
 * Process finished with exit code 0
 * <p>
 * 有些项目里用配置 @ConfigurationProperties("spring.datasource  来驱动自动数据源配置，详情还是要看 DataSourceAutoConfiguration 类内部
 */
@Slf4j
// @SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@SpringBootApplication
@EnableTransactionManagement
@PropertySource(value = {
        "classpath:META-INF/conf/application.properties"
})
//@Import({DDDSampleApplicationContext.class,
//        PathfinderApplicationContext.class})
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
        log.info("服务启动成功！you are welcome!");
    }

    /**
     * Try to test connectivity before bootstrapping completion. Originally we throw exception to stop tomcat here.
     * CommandLineRunner 接口的 Component 会在所有 Spring Beans 都初始化之后，SpringApplication.run() 之前执行，非常适合在应用程序启动之初进行一些数据初始化的工作。
     * 这些 bean 通常搭配 @Order 注解
     */
    @Component
    @Order(value = 1)
    static class ConnectionInitializer implements CommandLineRunner {

        @Autowired
        private ApplicationContext applicationContext;

        @Autowired
        private TransPayOrderPoMapper testMapper;

        @Autowired
        private PayOrderManager payOrderManager;

        @Override
        public void run(String... args) throws Exception {
            // 如果冷启动需要考虑网络加速问题，可以参考：https://blog.csdn.net/lxyoucan/article/details/111138711
            log.info(">>>>>>>>>>>>>>> Service is up, beginning to initialize datastore connection <<<<<<<<<<<<<");
            // 旗舰版的 idea 的facet 里可以开启 spring 的facet，可以更好地观测 spring beans
            log.info("all beans：" + Arrays.asList(applicationContext.getBeanDefinitionNames()));
            // 否则只能把 spring beans 注入进来，查看 bean 的调试视图，看看有没有可能看到正确的注入结果
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

            readSpecialFile();
        }

        private void readSpecialFile() throws IOException {
            InputStream inputStream = getClass().getResourceAsStream("/application.yml");
            final int available = inputStream.available();
            byte[] buffer = new byte[available];
            // 在缺省情况下 PushbackInputStream 能够 push back 的字符只有一个，超过 1 个就会出错：Push back buffer is full，我们需要把它提前初始化好
            PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream, available);
            int bytesRead = pushbackInputStream.read(buffer);
            // 当这一步读完了以后，inputStream.available() 就会降为 0
            String s1 = new String(buffer);
            log.info("Read " + bytesRead + " bytes: " + s1);
            // 回写对于底层的原始输入流而言，是无用的，回写的是这个输入流的 buf
            pushbackInputStream.unread(buffer);
            // 或者 pushbackInputStream.available()
            buffer = new byte[available];
            bytesRead = pushbackInputStream.read(buffer);
            final String s2 = new String(buffer);
            log.info("Read " + bytesRead + " bytes: " + s2);
        }

        /**
         * filter
         * destroy all resources.
         * This method will be invoked in Spring shutdown hook while gracefully shutting down.
         */
        @PreDestroy
        private void destroy() {
            // 标准的栈帧输出方式
            log.info("destroy taskCenter：" + Arrays.toString(Thread.currentThread().getStackTrace()));
        }
    }

}
