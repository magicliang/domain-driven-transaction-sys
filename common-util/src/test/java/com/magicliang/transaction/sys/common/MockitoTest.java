package com.magicliang.transaction.sys.common;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
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

        System.out.println(verify(mockedList).add("one"));
        verify(mockedList).clear();
    }
}
