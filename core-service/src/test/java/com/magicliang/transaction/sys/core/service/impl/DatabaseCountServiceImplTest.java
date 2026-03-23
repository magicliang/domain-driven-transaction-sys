package com.magicliang.transaction.sys.core.service.impl;

import com.magicliang.transaction.sys.common.dal.mybatis.mapper.DatabaseCountMapper;
import com.magicliang.transaction.sys.common.dal.mybatis.mapper.TableMetaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

/**
 * DatabaseCountServiceImpl 单元测试
 *
 * @author magicliang
 * date: 2026-03-23
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DatabaseCountServiceImplTest {

    @Mock
    private DatabaseCountMapper databaseCountMapper;

    @Mock
    private TableMetaMapper tableMetaMapper;

    @InjectMocks
    private DatabaseCountServiceImpl databaseCountService;

    @BeforeEach
    void setUp() {
        when(tableMetaMapper.selectAllTableNames())
                .thenReturn(Arrays.asList("tb_trans_pay_order", "tb_test_table"));
    }

    @Test
    void testGetTotalCountSuccess() {
        when(databaseCountMapper.countByTableName("tb_trans_pay_order")).thenReturn(100L);

        long count = databaseCountService.getTotalCount("tb_trans_pay_order");

        assertEquals(100L, count);
        verify(databaseCountMapper, times(1)).countByTableName("tb_trans_pay_order");
    }

    @Test
    void testGetTotalCountInvalidTableName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            databaseCountService.getTotalCount("invalid; DROP TABLE");
        });

        assertEquals("表名格式不合法", exception.getMessage());
        verify(databaseCountMapper, never()).countByTableName(anyString());
    }

    @Test
    void testGetTotalCountTableNotExists() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            databaseCountService.getTotalCount("non_existent_table");
        });

        assertEquals("表不存在: non_existent_table", exception.getMessage());
        verify(databaseCountMapper, never()).countByTableName(anyString());
    }

    @Test
    void testWhitelistCached() {
        when(databaseCountMapper.countByTableName("tb_trans_pay_order")).thenReturn(50L);

        databaseCountService.getTotalCount("tb_trans_pay_order");
        databaseCountService.getTotalCount("tb_trans_pay_order");

        verify(tableMetaMapper, times(1)).selectAllTableNames();
        verify(databaseCountMapper, times(2)).countByTableName("tb_trans_pay_order");
    }
}
