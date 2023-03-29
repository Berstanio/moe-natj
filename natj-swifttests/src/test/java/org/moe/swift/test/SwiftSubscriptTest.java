package org.moe.swift.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.moe.swift.test.bindings.SubscriptStruct;

public class SwiftSubscriptTest extends BaseSwiftTest {

    @Test
    public void testStructSubscript() {
        SubscriptStruct subscriptStruct = new SubscriptStruct();
        assertEquals(1, subscriptStruct.getSubscriptValue());
        assertEquals(5, subscriptStruct.get(5));
        subscriptStruct.setSubscriptValue(6);
        assertEquals(30, subscriptStruct.get(5));
        subscriptStruct.set(2, 10);
        assertEquals(20, subscriptStruct.getSubscriptValue());
        assertEquals(5 * 20, subscriptStruct.get(5));
    }
}
