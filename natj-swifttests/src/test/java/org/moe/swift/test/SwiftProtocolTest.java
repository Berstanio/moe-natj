package org.moe.swift.test;

import org.junit.jupiter.api.Test;
import org.moe.swift.test.bindings.protocolTests.ClassConformingProtocol;
import org.moe.swift.test.bindings.protocolTests.TestProtocol;
import static org.junit.jupiter.api.Assertions.*;

public class SwiftProtocolTest extends BaseSwiftTest {

    @Test
    public void testProtocolReturnKnownClassBinding() {
        TestProtocol testProtocol = ClassConformingProtocol.getClassAsProtocol();
        assertEquals(ClassConformingProtocol.class, testProtocol.getClass());
        assertEquals(3, testProtocol.protoFunc());
    }
}
