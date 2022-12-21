package org.moe.swift.test;

import org.junit.jupiter.api.Test;
import org.moe.swift.test.bindings.Global;
import org.moe.swift.test.bindings.protocolTests.ClassConformingProtocol;
import org.moe.swift.test.bindings.protocolTests.StructConformingProtocol;
import org.moe.swift.test.bindings.protocolTests.TestProtocol;

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
}
