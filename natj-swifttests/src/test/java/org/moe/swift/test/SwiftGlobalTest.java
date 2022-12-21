package org.moe.swift.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.moe.swift.test.bindings.Global;

public class SwiftGlobalTest extends BaseSwiftTest{

    @Test
    public void sumTestInt() {
        assertEquals(10, Global.intSum(7, 3));
        assertEquals(12901290 + 587, Global.intSum(587, 12901290));
    }

    @Test
    public void sumTestDouble() {
        assertEquals(11, Global.doubleSum(7.5, 3.5));
        assertEquals(12901290.3 + 587.6, Global.doubleSum(587.6, 12901290.3));
    }

    @Test
    public void sumTestFloat() {
        assertEquals(11, Global.floatSum(7.5f, 3.5f));
        assertEquals(1290129.3 + 587.6, Global.floatSum(587.6f, 1290129.3f), 0.1);
    }

    //@Test
    public void testChar() {
        assertEquals('b', Global.charTest('a'));
        assertEquals('a', Global.charTest('b'));
    }


    @Test
    public void testBool() {
        assertTrue(Global.boolTest(false));
        assertFalse(Global.boolTest(true));
    }

    @Test
    public void testStringUTF8() {
        assertEquals("Hey!", Global.stringTest("Hey"));
        assertEquals("Hey123!", Global.stringTest("Hey123"));
    }
}
