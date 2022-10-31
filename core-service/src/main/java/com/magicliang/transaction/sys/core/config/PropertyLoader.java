package com.magicliang.transaction.sys.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 一个 hocon 的 configuration 的例子
 *
 * @author magicliang
 * <p>
 * date: 2022-10-31 10:42
 */
@Slf4j
//@Configuration
public class PropertyLoader {

    @Bean
    @Autowired
    public static PropertySourcesPlaceholderConfigurer properties(Environment env) {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();

        // 这里要引入 typesafe 的 hocon 的依赖
//        Config conf = ConfigFactory.load();
//        conf.resolve();
//        TypesafePropertySource propertySource = new TypesafePropertySource("hoconSource", conf);

        ConfigurableEnvironment environment = (StandardEnvironment) env;
        MutablePropertySources propertySources = environment.getPropertySources();
//        propertySources.addLast(propertySource);
        pspc.setPropertySources(propertySources);

        return pspc;
    }


}

//class TypesafePropertySource extends PropertySource<Config>{
//    public TypesafePropertySource(String name, Config source) {
//        super(name, source);
//    }
//
//    @Override
//    public Object getProperty(String name) {
//        return this.getSource().getAnyRef(name);
//    }
//}