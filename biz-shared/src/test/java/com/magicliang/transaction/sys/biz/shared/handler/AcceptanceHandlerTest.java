package com.magicliang.transaction.sys.biz.shared.handler;

import com.magicliang.transaction.sys.biz.shared.request.acceptance.AcceptanceCommand;
import com.magicliang.transaction.sys.biz.shared.request.callback.CallbackCommand;
import com.magicliang.transaction.sys.common.util.ReflectionUtil;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2022-12-30 16:07
 */
class AcceptanceHandlerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * 这个实验证明，我们可以验证多参数的继承关系
     */
    @Test
    void testIsGenericSubClass() {
        Assertions.assertTrue(ReflectionUtil.isGenericSubClass(AcceptanceHandler.class, BaseHandler.class, AcceptanceCommand.class));
        Assertions.assertTrue(ReflectionUtil.isGenericSubClass(AcceptanceHandler.class, BaseHandler.class, TransactionModel.class));
        Assertions.assertFalse(ReflectionUtil.isGenericSubClass(AcceptanceHandler.class, BaseHandler.class, CallbackCommand.class));
    }

    @Test
    void testIsParameterized() {
        Assertions.assertTrue(ReflectionUtil.isParameterized(AcceptanceHandler.class, BaseHandler.class, AcceptanceCommand.class));
        Assertions.assertFalse(ReflectionUtil.isParameterized(AcceptanceHandler.class, BaseHandler.class, CallbackCommand.class));
    }

}