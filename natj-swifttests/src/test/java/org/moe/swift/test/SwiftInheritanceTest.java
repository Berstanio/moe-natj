package org.moe.swift.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.moe.natj.swift.SwiftRuntime;
import org.moe.swift.test.bindings.BaseClass;
import org.moe.swift.test.bindings.Global;
import org.moe.swift.test.bindings.JavaSideSubClass;
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

    @Test
    public void testJavaSideSubClass() {
        JavaSideSubClass javaSideSubClass = new JavaSideSubClass();
        assertEquals(SwiftRuntime.getMetadataForClass(javaSideSubClass.getClass()), SwiftRuntime.dereferencePeer(javaSideSubClass.getPeerPointer()));
        assertEquals(42, javaSideSubClass.getClassSpecificNumber());
        assertEquals(6, javaSideSubClass.getBaseClassField());
        assertEquals(42, JavaSideSubClass.getClassNumberGlobal(javaSideSubClass));
        // TODO: 19.12.22 Implement tests to ensure, that java class is correctly rematched to swift class
        // TODO: 19.12.22 Implement tests, to ensure that binding class always recreate a new class
    }

    @Test
    public void testJavaSideSubClassKeepsValue() {
        JavaSideSubClass javaSideSubClass = new JavaSideSubClass();
        assertEquals(42, JavaSideSubClass.getClassNumberGlobal(javaSideSubClass));
        JavaSideSubClass javaSideSubClass2 = (JavaSideSubClass) JavaSideSubClass.returnBaseClassAgain(javaSideSubClass);
        assertEquals(javaSideSubClass, javaSideSubClass2);
        assertEquals(42, JavaSideSubClass.getClassNumberGlobal(javaSideSubClass2));
        javaSideSubClass.testValue = 55;
        javaSideSubClass2 = (JavaSideSubClass) JavaSideSubClass.returnBaseClassAgain(javaSideSubClass);
        assertEquals(55, JavaSideSubClass.getClassNumberGlobal(javaSideSubClass));
        assertEquals(55, JavaSideSubClass.getClassNumberGlobal(javaSideSubClass2));
    }

    @Test
    public void testBindingClassNotConsistent() {
        BaseClass b1 = new BaseClass();
        BaseClass b2 = JavaSideSubClass.returnBaseClassAgain(b1);
        BaseClass b3 = JavaSideSubClass.returnBaseClassAgain(b2);
        BaseClass b4 = JavaSideSubClass.returnBaseClassAgain(b3);
        assertNotEquals(b1, b2);
        assertNotEquals(b2, b3);
        assertNotEquals(b3, b4);
    }
}
