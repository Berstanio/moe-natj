package org.moe.swift.test;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.moe.swift.test.bindings.Global;

public class SwiftGlobalTest extends BaseSwiftTest{

    @Test
    public void sumTest() {
        assertEquals(10, Global.sum(7, 3));
        assertEquals(12901290 + 587, Global.sum(587, 12901290));
    }
}
