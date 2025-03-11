package com.mlersblog;

import lombok.Data;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LombokTest {

    @Test
    public void testLombokGetterSetter() {
        // 创建一个使用@Data注解的测试对象
        TestData testData = new TestData();
        
        // 测试setter
        testData.setName("测试名称");
        testData.setValue(100);
        
        // 测试getter
        assertEquals("测试名称", testData.getName());
        assertEquals(100, testData.getValue());
        
        // 测试toString
        assertTrue(testData.toString().contains("测试名称"));
        assertTrue(testData.toString().contains("100"));
        
        // 测试equals和hashCode
        TestData sameData = new TestData();
        sameData.setName("测试名称");
        sameData.setValue(100);
        
        assertEquals(testData, sameData);
        assertEquals(testData.hashCode(), sameData.hashCode());
    }
    
    @Data
    static class TestData {
        private String name;
        private int value;
    }
} 