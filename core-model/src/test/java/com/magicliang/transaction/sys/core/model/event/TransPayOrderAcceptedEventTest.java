package com.magicliang.transaction.sys.core.model.event;

import com.magicliang.transaction.sys.common.util.ReflectionUtil;
import com.magicliang.transaction.sys.core.shared.DomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.ResolvableType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2022-12-30 11:41
 */
@Slf4j
class TransPayOrderAcceptedEventTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetActualType() {
        final Class<TransPayOrderAcceptedEvent> rawClass = TransPayOrderAcceptedEvent.class;
        final Type rawType = rawClass;

        // 这一步其实走不进来，因为这个类型不是泛型类型，如果这个类型本身是泛型类型，则可能走进来
        if (rawType instanceof ParameterizedType) {
            final TypeVariable<Class<TransPayOrderAcceptedEvent>>[] typeParameters = rawClass.getTypeParameters();
            log.info(Arrays.toString(typeParameters));

            Type[] actualTypeArguments = ((ParameterizedType) rawType).getActualTypeArguments();
            log.info("" + Arrays.toString(actualTypeArguments));
        }

        Assertions.assertTrue(true);

        // 接口可能是参数化类型，也可能不是
        final Class<?>[] interfaces = rawClass.getInterfaces();
        for (Class<?> singleInterface : interfaces) {
            Type singleInterfaceType = singleInterface;

            // 如果一个类型是泛型接口，则它不再有泛型超类，对于 Type 子类型的辨析，需要通过赋值给 Type 来做，泛型接口的 Class 仍然是 class，不是 ParameterizedType，所以此处返回 false
            if (singleInterfaceType instanceof ParameterizedType) {
                // 获取本接口的实际类型变量的 types，类型为 Type
                final Type[] actualTypeArguments = ((ParameterizedType) singleInterfaceType).getActualTypeArguments();
                log.info(Arrays.toString(actualTypeArguments));
            }

            // 获取 本接口的 TypeParameters，类型为 TypeVariable，这里的 genericTypeParameter 为 T，不是实际类型！
            final TypeVariable<? extends Class<?>>[] genericTypeParameter = singleInterface.getTypeParameters();

            // 如果过还有超类，看看超类
            final Type genericSuperclass = singleInterface.getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                final TypeVariable<? extends Class<?>>[] typeParameters = singleInterface.getTypeParameters();
                log.info(Arrays.toString(typeParameters));

                Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
                log.info(Arrays.toString(actualTypeArguments));
            }
        }

        // 获取类型在基接口里的泛型的方法在此
        final Type[] genericInterfaceTypes = rawClass.getGenericInterfaces();
        for (Type genericInterfaceType : genericInterfaceTypes) {
            if (genericInterfaceType instanceof ParameterizedType) {
                // 获取本接口的实际类型变量的 types，类型为 Type
                final Type[] actualTypeArguments = ((ParameterizedType) genericInterfaceType).getActualTypeArguments();
                for (Type type : actualTypeArguments) {
                    log.info(type.getTypeName());
                    if (type instanceof Class) {
                        log.info(((Class<?>) type).isAssignableFrom(TransPayOrderAcceptedEvent.class) + "");
                    }
                }
                log.info(Arrays.toString(actualTypeArguments));
            }

        }

        final ResolvableType generic = ResolvableType.forClass(rawClass).as(ApplicationListener.class).getGeneric();
        log.info(generic.toString());
    }

    @Test
    void testIsGenericSubClass() {
        // TransPayOrderPaidEvent extends TransPayOrderAcceptedEvent extends ApplicationEvent，TransPayOrderPaidEvent/TransPayOrderAcceptedEvent 为 true，TransPayOrderPaidEvent 为 false
        Assertions.assertTrue(ReflectionUtil.isGenericSubClass(TransPayOrderAcceptedEvent.class, DomainEvent.class, TransPayOrderAcceptedEvent.class));
        Assertions.assertTrue(ReflectionUtil.isGenericSubClass(TransPayOrderAcceptedEvent.class, DomainEvent.class, TransPayOrderPaidEvent.class));
        Assertions.assertFalse(ReflectionUtil.isGenericSubClass(TransPayOrderAcceptedEvent.class, DomainEvent.class, ApplicationEvent.class));
        Assertions.assertFalse(ReflectionUtil.isGenericSubClass(TransPayOrderAcceptedEvent.class, DomainEvent.class, Object.class));
    }

    @Test
    void testIsParameterized() {
        // TransPayOrderPaidEvent extends TransPayOrderAcceptedEvent extends ApplicationEvent，TransPayOrderPaidEvent/TransPayOrderAcceptedEvent 为 true，TransPayOrderPaidEvent 为 false
        Assertions.assertTrue(ReflectionUtil.isParameterized(TransPayOrderAcceptedEvent.class, DomainEvent.class, TransPayOrderAcceptedEvent.class));
        Assertions.assertTrue(ReflectionUtil.isParameterized(TransPayOrderAcceptedEvent.class, DomainEvent.class, TransPayOrderPaidEvent.class));
        Assertions.assertFalse(ReflectionUtil.isParameterized(TransPayOrderAcceptedEvent.class, DomainEvent.class, ApplicationEvent.class));
        Assertions.assertFalse(ReflectionUtil.isParameterized(TransPayOrderAcceptedEvent.class, DomainEvent.class, Object.class));
    }

}