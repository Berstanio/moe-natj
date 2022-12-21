package org.moe.swift.test.bindings.protocolTests;

import org.moe.natj.general.NatJ;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.SwiftProtocol;
import org.moe.natj.swift.ann.VirtualSwiftMethod;

@SwiftProtocol(protocolDescriptor = "$s9swiftTest0B8ProtocolMp")
@Runtime(SwiftRuntime.class)
public interface TestProtocol {

    @VirtualSwiftMethod(offset = 8)
    public long protoFunc();
}
