package org.moe.swift.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.moe.swift.test.bindings.TestBigStruct;
import org.moe.swift.test.bindings.TestMediumStruct;
import org.moe.swift.test.bindings.TestStruct;

public class SwiftStructTest extends BaseSwiftTest {

    @Test
    public void testInit() {
        TestStruct struct = new TestStruct();
        assertEquals(5, struct.getField());
        assertEquals(7, struct.getRandomField());
        assertEquals(5 + 4, TestStruct.structTest(struct, 4));
        struct.setField(15);
        struct.setRandomField(13);
        assertEquals(15, struct.getField());
        assertEquals(13, struct.getRandomField());
        assertEquals(15 + 4, TestStruct.structTest(struct, 4));
    }

    @Test
    public void testStructMethod() {
        TestStruct struct = new TestStruct();
        long randomField = struct.getRandomField();
        assertEquals(7, randomField);
        long par1 = 5;
        assertEquals(randomField + par1, struct.getStructNumber(par1));
    }

    @Test
    public void testMutatingMethod() {
        TestStruct struct = new TestStruct();
        assertEquals(5, struct.getField());
        struct.mutateTest(17);
        assertEquals(17, struct.getField());
    }

    @Test
    public void testStructMethodNoParameter() {
        TestStruct struct = new TestStruct();
        long randomField = struct.getRandomField();
        assertEquals(7, randomField);
        assertEquals(randomField, struct.getStructNumber());
    }

    @Test
    public void testBigStruct() {
        TestBigStruct testBigStruct = new TestBigStruct();
        assertEquals(4, testBigStruct.getField4());
        assertEquals(5, TestBigStruct.structTest(testBigStruct, 1));
        assertEquals(5, testBigStruct.getStructNumber(1));
        assertEquals(4, testBigStruct.getStructNumber());
    }

    @Test
    public void testMediumStruct() {
        TestMediumStruct testMediumStruct = new TestMediumStruct();
        assertEquals(1, testMediumStruct.getField1());
        assertEquals(2, testMediumStruct.getField2());
        assertEquals(3, testMediumStruct.getField3());
        assertEquals(4, testMediumStruct.getField4());
        assertEquals(5, TestMediumStruct.structTest(testMediumStruct, 1));
        assertEquals(4, testMediumStruct.getStructNumber(1));
        assertEquals(3, testMediumStruct.getStructNumber());
    }
}
