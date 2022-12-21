package org.moe.swift.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.moe.natj.swift.SwiftRuntime;
import org.moe.swift.test.bindings.BaseClass;
import org.moe.swift.test.bindings.Global;
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
    public void testVirtualDispatch() {
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
        assertNotEquals(BaseClass.getType(), SwiftRuntime.dereferencePeer(baseClass.getPeerPointer()));
        assertEquals(SubClass.getType(), SwiftRuntime.dereferencePeer(baseClass.getPeerPointer()));
        assertEquals(SubClass.class, baseClass.getClass());
    }

    @Test
    public void testIncompleteImplicitUpcasting() {
        BaseClass baseClass = Global.getUnknownSubClassAsBaseClass();
        assertNotEquals(BaseClass.getType(), SwiftRuntime.dereferencePeer(baseClass.getPeerPointer()));
        assertNotEquals(SubClass.getType(), SwiftRuntime.dereferencePeer(baseClass.getPeerPointer()));
        assertEquals(SubClass.class, baseClass.getClass());
        assertEquals(3, baseClass.getClassSpecificNumber());
    }
}
