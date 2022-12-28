package org.moe.swift.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.moe.swift.test.bindings.LargeTestEnum;
import org.moe.swift.test.bindings.TestEnum;

public class SwiftEnumTest extends BaseSwiftTest {

    @Test
    public void testRetrieveEnum() {
        TestEnum testEnum = TestEnum.getTestEnum();
        assertEquals(TestEnum.NotEmptyTestCase.class, testEnum.getClass());
        assertEquals(19, ((TestEnum.NotEmptyTestCase) testEnum).getField1());
    }

    @Test
    public void testRetrieveEnumLarge() {
        LargeTestEnum testEnum = LargeTestEnum.getTestEnum();
        assertEquals(LargeTestEnum.SecondTestCase.class, testEnum.getClass());
        assertEquals(19, ((LargeTestEnum.SecondTestCase) testEnum).getField1());
        assertEquals(7, ((LargeTestEnum.SecondTestCase) testEnum).getField2());
    }

    @Test
    public void testPassEmptyEnum() {
        TestEnum testEnum = new TestEnum.EmptyTestCase();
        assertEquals(7, TestEnum.passTestEnum(testEnum));
    }

    @Test
    public void testPassLargeEmptyEnum() {
        LargeTestEnum testEnum = new LargeTestEnum.EmptyTestCase();
        assertEquals(7, LargeTestEnum.passTestEnum(testEnum));
    }

    @Test
    public void testPassOneEntryEnum() {
        TestEnum testEnum = new TestEnum.NotEmptyTestCase(17);
        assertEquals(17, TestEnum.passTestEnum(testEnum));
    }

    @Test
    public void testPassLargeOneEntryEnum() {
        LargeTestEnum testEnum = new LargeTestEnum.NotEmptyTestCase(17);
        assertEquals(17, LargeTestEnum.passTestEnum(testEnum));
    }

    @Test
    public void testPassSecondOneEntryEnum() {
        TestEnum testEnum = new TestEnum.SecondTestCase(15);
        assertEquals(16, TestEnum.passTestEnum(testEnum));
    }

    @Test
    public void testPassLargeSecondEntryEnum() {
        LargeTestEnum testEnum = new LargeTestEnum.SecondTestCase(15, 10);
        assertEquals(25, LargeTestEnum.passTestEnum(testEnum));
    }

    @Test
    public void testPassFloatEntryEnum() {
        TestEnum testEnum = new TestEnum.FloatTestCase(15.7f);
        assertEquals(15, TestEnum.passTestEnum(testEnum));
    }

    @Test
    public void testPassLargeLargeEntryEnum() {
        LargeTestEnum testEnum = new LargeTestEnum.LargeTestCase(15, 10, 5, 7, 3);
        assertEquals(40, LargeTestEnum.passTestEnum(testEnum));
    }

    @Test
    public void callEnumFunction() {
        TestEnum testEnum1 = new TestEnum.NotEmptyTestCase(6);
        assertEquals(10, testEnum1.testEnumFunction(4));
        TestEnum testEnum2 = new TestEnum.SecondTestCase(7);
        assertEquals(9, testEnum2.testEnumFunction(2));
        TestEnum testEnum3 = new TestEnum.EmptyTestCase();
        assertEquals(4, testEnum3.testEnumFunction(3));
        TestEnum testEnum4 = new TestEnum.FloatTestCase(10.5f);
        assertEquals(16, testEnum4.testEnumFunction(6));
    }

    @Test
    public void callLargeEnumFunction() {
        LargeTestEnum testEnum3 = new LargeTestEnum.EmptyTestCase();
        assertEquals(4, testEnum3.testEnumFunction(3));
        LargeTestEnum testEnum1 = new LargeTestEnum.NotEmptyTestCase(6);
        assertEquals(10, testEnum1.testEnumFunction(4));
        LargeTestEnum testEnum2 = new LargeTestEnum.SecondTestCase(7, 3);
        assertEquals(12, testEnum2.testEnumFunction(2));
        LargeTestEnum testEnum4 = new LargeTestEnum.LargeTestCase(15, 10, 5, 7, 3);
        assertEquals(46, testEnum4.testEnumFunction(6));
    }
}
