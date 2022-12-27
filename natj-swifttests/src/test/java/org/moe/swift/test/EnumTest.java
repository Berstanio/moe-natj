package org.moe.swift.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.moe.swift.test.bindings.TestEnum;

public class EnumTest extends BaseSwiftTest {

    @Test
    public void testRetrieveEnum() {
        TestEnum testEnum = TestEnum.getTestEnum();
        assertEquals(TestEnum.NotEmptyTestCase.class, testEnum.getClass());
        assertEquals(19, ((TestEnum.NotEmptyTestCase) testEnum).getField1());
    }

    @Test
    public void testPassEmptyEnum() {
        TestEnum testEnum = new TestEnum.EmptyTestCase();
        assertEquals(7, TestEnum.passTestEnum(testEnum));
    }

    @Test
    public void testPassOneEntryEnum() {
        TestEnum testEnum = new TestEnum.NotEmptyTestCase(17);
        assertEquals(17, TestEnum.passTestEnum(testEnum));
    }

    @Test
    public void testPassSecondOneEntryEnum() {
        TestEnum testEnum = new TestEnum.SecondTestCase(15);
        assertEquals(16, TestEnum.passTestEnum(testEnum));
    }

    @Test
    public void testPassFloatEntryEnum() {
        TestEnum testEnum = new TestEnum.FloatTestCase(15.7f);
        assertEquals(15, TestEnum.passTestEnum(testEnum));
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
}
