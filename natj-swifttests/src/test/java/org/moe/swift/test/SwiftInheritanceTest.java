package org.moe.swift.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.moe.swift.test.bindings.BaseClass;
import org.moe.swift.test.bindings.SubClass;

public class SwiftInheritanceTest extends BaseSwiftTest {

    @Test
    public void test() {
        SubClass subClass = new SubClass();
        assertEquals(2, subClass.getClassSpecificNumber());
        BaseClass baseClass = new BaseClass(subClass.getPeer());
        assertEquals(2, baseClass.getClassSpecificNumber());
        BaseClass realBaseClass = new BaseClass();
        assertEquals(1, realBaseClass.getClassSpecificNumber());
    }
}
