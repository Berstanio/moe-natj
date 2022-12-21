package org.moe.swift.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.moe.natj.swift.SwiftRuntime;
import org.moe.swift.test.bindings.BaseClass;
import org.moe.swift.test.bindings.SubClass;

public class SwiftInheritanceTest extends BaseSwiftTest {

    @Test
    public void testOnlySubclass() {
        SubClass subClass = new SubClass();
        assertEquals(5, subClass.onlySubClass());
    }

    @Test
    public void testOnlyBaseclass() {
        SubClass subClass = new SubClass();
        assertEquals(6, subClass.getBaseClassField());
        BaseClass baseClass = new BaseClass();
        assertEquals(6, baseClass.getBaseClassField());
    }

    @Test
    public void testOverride() {
        SubClass subClass = new SubClass();
        assertEquals(2, subClass.getClassSpecificNumber());
        BaseClass baseClass = new BaseClass(subClass.getPeer());
        assertEquals(2, baseClass.getClassSpecificNumber());
        BaseClass realBaseClass = new BaseClass();
        assertEquals(1, realBaseClass.getClassSpecificNumber());
    }

    @Test
    public void testImplicitUpcasting() {
        BaseClass baseClass = SubClass.getSubClassAsBaseClass();
        assertNotEquals(BaseClass.getType(), SwiftRuntime.getTypeOfPointer(baseClass.getPeerPointer()));
        assertEquals(SubClass.getType(), SwiftRuntime.getTypeOfPointer(baseClass.getPeerPointer()));
        assertEquals(SubClass.class, baseClass.getClass());
    }
}
