package com.magicliang.transaction.sys;

import ch.vorburger.mariadb4j.springboot.autoconfigure.DataSourceAutoConfiguration;
import com.magicliang.transaction.sys.biz.shared.event.ApplicationEvents;
import com.magicliang.transaction.sys.biz.shared.handler.BaseHandler;
import com.magicliang.transaction.sys.biz.shared.request.HandlerRequest;
import com.magicliang.transaction.sys.common.util.ReflectionUtil;
import com.magicliang.transaction.sys.core.model.context.TransTransactionContext;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.event.TransPayOrderAcceptedEvent;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 基于 Spring 的集成测试层，每次跑测试都要启动 Spring 容器，只有集成测试才需要使用这个东西
 * <p>
 * 标准的集成测试要求：
 * 1. 启动完整的上下文，而没 mockEnviroment，mockMvc
 * 2. 连接真正的 enterprise infrastructure，即连接真正的数据库
 * 3. deploy a real server，占据真实的端口号
 * <p>
 * 这些测试比较慢，是 Time Consuming 的，通常在 check 的比较靠后的一步，在单元测试后执行。
 * <p>
 * 传统的测试通常还需要 @RunWith(SpringRunner.class)，但这个 SpringRunner 实际上是一个 JUnit 4 的 runner 的子类，在当代应该被逐渐淘汰了
 * 博客实战：：
 * https://www.baeldung.com/integration-testing-in-spring
 * https://www.baeldung.com/spring-boot-testing
 * https://zhuanlan.zhihu.com/p/111418479
 * https://ithelp.ithome.com.tw/articles/10196471
 * https://www.cnblogs.com/myitnews/p/12330297.html
 * 官方原理：
 * https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#integration-testing
 * https://spring.io/guides/gs/testing-web/
 * </p>
 *
 * @author magicliang
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootTest(classes = {DomainDrivenTransactionSysApplicationIntegrationTest.class})
public class DomainDrivenTransactionSysApplicationIntegrationTest {

    private List<? extends Number> listNum;

    private List<? super String> listStr;

    @Autowired
    private ApplicationEvents applicationEvents;

    /**
     * 处理器列表
     */
    @Autowired
    private List<BaseHandler<? extends HandlerRequest, ? extends TransactionModel, ? extends TransTransactionContext>> handlers;

    @Test
    void contextLoads() {
        Assertions.assertTrue(true);
    }

    @Test
    void testEvents() {
        final TransPayOrderEntity payOrder = new TransPayOrderEntity();
        payOrder.setPayOrderNo(ThreadLocalRandom.current().nextLong());
        applicationEvents.transPayOrderAccepted(TransPayOrderAcceptedEvent.create(this, payOrder));
        Assertions.assertTrue(true);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetWildCardType() throws NoSuchFieldException {
        if (CollectionUtils.isEmpty(handlers)) {
            Assertions.assertTrue(true);
            return;
        }

        // 这里虽然声明的时候只有通配符，但得到每一个具体的 Handler 的时候，通配符已经被类型实参所取代了，第三个参数 TransTransactionContext 也不过是 ParameterizedTypeImpl
        final BaseHandler<? extends HandlerRequest, ? extends TransactionModel, ? extends TransTransactionContext> baseHandler = handlers.get(
                0);
        final Collection<?> wildCardType = ReflectionUtil.getWildCardType(baseHandler.getClass());

        final Field fieldNum = DomainDrivenTransactionSysApplicationIntegrationTest.class.getDeclaredField("listNum");
        // genericType几乎必然是 ParameterizedTypeImpl 的实例
        final Type genericType = fieldNum.getGenericType();
        final ParameterizedType parameterizedType = (ParameterizedType) genericType;
        final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        for (Type actualTypeArgument : actualTypeArguments) {
            // wildCard 隐藏在 actualTypeArguments 里，不隐藏在
            if (actualTypeArgument instanceof WildcardType) {
                final WildcardType wildcardType = (WildcardType) actualTypeArgument;
                final Type[] lowerBounds = wildcardType.getLowerBounds();
                // 所有的 bound 都是 class
                log.info(Arrays.toString(lowerBounds));
                final Type[] upperBounds = wildcardType.getUpperBounds();
                log.info(Arrays.toString(upperBounds));
            }
        }

        Assertions.assertTrue(true);
    }

}