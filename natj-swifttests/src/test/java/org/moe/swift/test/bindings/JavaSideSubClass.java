package org.moe.swift.test.bindings;

import org.moe.natj.general.NatJ;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.StaticSwiftMethod;
import org.moe.natj.swift.ann.VirtualSwiftMethod;
import org.moe.swift.test.bindings.protocolTests.TestProtocol;

@Runtime(SwiftRuntime.class)
public class JavaSideSubClass extends BaseClass implements TestProtocol {

    static {
        NatJ.register();
    }

    public long testValue = 42;

    public JavaSideSubClass(Pointer peer) {
        super(peer);
    }

    public JavaSideSubClass() {
        super();
    }


    @Override
    @VirtualSwiftMethod(offset = 88)
    public long getClassSpecificNumber() {
        return testValue;
    }

    @StaticSwiftMethod(symbol = "$s9swiftTest28globalGetClassSpecificNumberySiAA04BaseE0CF")
    public static native long getClassNumberGlobal(BaseClass baseClass);

    @StaticSwiftMethod(symbol = "$s9swiftTest26globalReturnBaseClassAgainyAA0eF0CADF")
    public static native BaseClass returnBaseClassAgain(BaseClass baseClass);

    @Override
    @VirtualSwiftMethod(offset = 8)
    public long protoFunc() {
        return 122;
    }
}
