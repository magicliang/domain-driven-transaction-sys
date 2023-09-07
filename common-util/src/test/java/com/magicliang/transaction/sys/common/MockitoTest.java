package com.magicliang.transaction.sys.common;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 *
 * description:
 *
 * @author magicliang
 *
 *         date: 2023-09-07 14:09
 */
public class MockitoTest {

    @Test
    public void testVerification() {
        //mock creation
        List mockedList = mock(List.class);

        //using mock object
        mockedList.add("one");
        mockedList.clear();

        //verification
        verify(mockedList).clear();
        System.out.println(verify(mockedList).add("one"));

        /*
         * Wanted but not invoked:
list.stream();
-> at com.magicliang.transaction.sys.common.MockitoTest.testVerification(MockitoTest.java:32)

However, there were exactly 2 interactions with this mock:
list.add("one");
-> at com.magicliang.transaction.sys.common.MockitoTest.testVerification(MockitoTest.java:26)

list.clear();
-> at com.magicliang.transaction.sys.common.MockitoTest.testVerification(MockitoTest.java:27)
         */
        // verify(mockedList).stream();

        Assertions.assertTrue(true);
    }
}
