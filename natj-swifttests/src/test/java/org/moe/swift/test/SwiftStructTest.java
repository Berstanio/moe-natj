package org.moe.swift.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
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
        assertEquals(randomField + par1, TestStruct.getStructNumber(par1, struct));
    }
}
