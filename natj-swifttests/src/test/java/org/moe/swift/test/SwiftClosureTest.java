package org.moe.swift.test;

import org.junit.jupiter.api.Test;
import org.moe.swift.test.bindings.Global;

import static org.junit.jupiter.api.Assertions.*;

public class SwiftClosureTest extends BaseSwiftTest {

    @Test
    public void testPassClosure() {
        Global.Callback callback = new Global.Callback() {
            @Override
            public long callback_function(long par) {
                return par + 17;
            }
        };
        assertEquals(29, Global.closurePassTest(7, callback));
        assertEquals(29, Global.closurePassTest(7, callback));
        assertEquals(28, Global.closurePassTest(7, (par) -> par + 16));
    }

    //@Test
    public void testReturnPassedClosure() {
        Global.Callback callback = par -> par + 17;
        Global.Callback returned = Global.closureReturnTest(7, callback);
        assertEquals(callback, returned);
        assertEquals(20, returned.callback_function(3));
    }
}
