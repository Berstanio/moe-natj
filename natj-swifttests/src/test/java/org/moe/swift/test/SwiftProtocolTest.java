package org.moe.swift.test;

import org.junit.jupiter.api.Test;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.swift.test.bindings.Global;
import org.moe.swift.test.bindings.JavaSideSubClass;
import org.moe.swift.test.bindings.protocolTests.*;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.*;

public class SwiftProtocolTest extends BaseSwiftTest {

    @Test
    public void testProtocolReturnKnownClassBinding() {
        TestProtocol testProtocol = ClassConformingProtocol.getClassAsProtocol();
        assertEquals(ClassConformingProtocol.class, testProtocol.getClass());
        assertEquals(3, testProtocol.protoFunc());
    }

    @Test
    public void testProtocolReturnKnownLargeStructBinding() {
        TestProtocol testProtocol = StructConformingProtocol.getStructAsProtocol();
        assertEquals(StructConformingProtocol.class, testProtocol.getClass());
        assertEquals(2, testProtocol.protoFunc());
        StructConformingProtocol structConformingProtocol = (StructConformingProtocol) testProtocol;
        assertEquals(11, structConformingProtocol.getField2());
        structConformingProtocol.setField3(22);
        assertEquals(22, structConformingProtocol.getField3());
    }

    @Test
    public void testProtocolReturnKnownSmallStructBinding() {
        TestProtocol testProtocol = SmallStructConformingProtocol.getSmallStructAsProtocol();
        assertEquals(SmallStructConformingProtocol.class, testProtocol.getClass());
        assertEquals(11, testProtocol.protoFunc());
        SmallStructConformingProtocol smallStructConformingProtocol = (SmallStructConformingProtocol) testProtocol;
        assertEquals(10, smallStructConformingProtocol.getField());
        assertEquals(11, smallStructConformingProtocol.getField2());
        smallStructConformingProtocol.setField2(22);
        assertEquals(22, smallStructConformingProtocol.getField2());
        assertEquals(22, testProtocol.protoFunc());
    }

    @Test
    public void testProtocolReturnUnknownBinding() {
        TestProtocol testProtocol = Global.getUnknownProtocolClass();
        assertTrue(Proxy.isProxyClass(testProtocol.getClass()));
        assertEquals(7, testProtocol.protoFunc());
    }

    @Test
    public void testProtocolPassUnknownBinding() {
        TestProtocol testProtocol = Global.getUnknownProtocolClass();
        assertTrue(Proxy.isProxyClass(testProtocol.getClass()));
        assertEquals(7, Global.passBackUnknownProtocol(testProtocol));
    }

    @Test
    public void testProtocolPassKnownClassBinding() {
        TestProtocol testProtocol = ClassConformingProtocol.getClassAsProtocol();
        assertEquals(ClassConformingProtocol.class, testProtocol.getClass());
        assertEquals(3, testProtocol.protoFunc());
        assertEquals(3, Global.passBackUnknownProtocol(testProtocol));
    }

    @Test
    public void testProtocolPassKnownStructBinding() {
        TestProtocol testProtocol = StructConformingProtocol.getStructAsProtocol();
        assertEquals(StructConformingProtocol.class, testProtocol.getClass());
        assertEquals(2, testProtocol.protoFunc());
        assertEquals(2, Global.passBackUnknownProtocol(testProtocol));
    }

    @Test
    public void testProtocolPassKnownSmallStructBinding() {
        TestProtocol testProtocol = SmallStructConformingProtocol.getSmallStructAsProtocol();
        assertEquals(SmallStructConformingProtocol.class, testProtocol.getClass());
        assertEquals(11, testProtocol.protoFunc());
        assertEquals(11, Global.passBackUnknownProtocol(testProtocol));
    }

    @Test
    public void testPassJavaSideProtocol() {
        JavaSideProtocol javaSideProtocol = new JavaSideProtocol();
        assertEquals(77, javaSideProtocol.protoFunc());
        assertEquals(77, Global.passBackUnknownProtocol(javaSideProtocol));
    }

    @Test
    public void testPassJavaSideSubClassConformingToProtocol() {
        JavaSideSubClass javaSideProtocol = new JavaSideSubClass();
        assertEquals(SwiftRuntime.getMetadataForClass(javaSideProtocol.getClass()), SwiftRuntime.dereferencePeer(javaSideProtocol.getPeerPointer()));
        assertEquals(122, javaSideProtocol.protoFunc());
        assertEquals(122, Global.passBackUnknownProtocol(javaSideProtocol));
    }

    @Test
    public void testProtocolReturnKnownEnumBinding() {
        TestProtocol testProtocol = TestEnumConformingProtocol.getEnumAsProtocol();
        assertEquals(TestEnumConformingProtocol.SecondTestCase.class, testProtocol.getClass());
        assertEquals(15, ((TestEnumConformingProtocol.SecondTestCase) testProtocol).getField1());
        assertEquals(15, testProtocol.protoFunc());
    }

    @Test
    public void testProtocolPassKnownEnumBinding() {
        TestProtocol testProtocol = TestEnumConformingProtocol.getEnumAsProtocol();
        assertEquals(TestEnumConformingProtocol.SecondTestCase.class, testProtocol.getClass());
        assertEquals(15, testProtocol.protoFunc());
        assertEquals(15, Global.passBackUnknownProtocol(testProtocol));

        TestProtocol proto2 = new TestEnumConformingProtocol.NotEmptyTestCase(7);
        assertEquals(7, proto2.protoFunc());
        assertEquals(7, Global.passBackUnknownProtocol(proto2));
    }

    @Test
    public void testProtocolReturnKnownLargeEnumBinding() {
        TestProtocol testProtocol = LargeTestEnumConformingProtocol.getEnumAsProtocol();
        assertEquals(LargeTestEnumConformingProtocol.SecondTestCase.class, testProtocol.getClass());
        assertEquals(15, ((LargeTestEnumConformingProtocol.SecondTestCase) testProtocol).getField1());
        assertEquals(20, ((LargeTestEnumConformingProtocol.SecondTestCase) testProtocol).getField2());
        assertEquals(35, testProtocol.protoFunc());
    }

    @Test
    public void testProtocolPassKnownLargeEnumBinding() {
        TestProtocol testProtocol = LargeTestEnumConformingProtocol.getEnumAsProtocol();
        assertEquals(LargeTestEnumConformingProtocol.SecondTestCase.class, testProtocol.getClass());
        assertEquals(35, testProtocol.protoFunc());
        assertEquals(35, Global.passBackUnknownProtocol(testProtocol));

        TestProtocol proto2 = new LargeTestEnumConformingProtocol.LargeTestCase(7, 3, 2, 8, 5);
        assertEquals(25, proto2.protoFunc());
        assertEquals(25, Global.passBackUnknownProtocol(proto2));
    }
}
