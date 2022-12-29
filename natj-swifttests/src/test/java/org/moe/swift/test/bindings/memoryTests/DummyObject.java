package org.moe.swift.test.bindings.memoryTests;

import org.moe.natj.general.NatJ;
import org.moe.natj.general.NativeObject;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.Owned;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.StaticSwiftMethod;
import org.moe.natj.swift.ann.SwiftBindingClass;
import org.moe.natj.swift.ann.SwiftConstructor;

@Runtime(SwiftRuntime.class)
@SwiftBindingClass
public class DummyObject extends NativeObject {

    static {
        NatJ.register();
    }

    protected DummyObject(Pointer peer) {
        super(peer);
    }

    public DummyObject() {
        super(SwiftRuntime.createStrongPointer(constructor(getType()), true));
    }

    @StaticSwiftMethod(symbol = "$s9swiftTest11DummyObjectCMa")
    public static native long getType();

    @SwiftConstructor
    @StaticSwiftMethod(symbol = "$s9swiftTest11DummyObjectCACycfC")
    private static native long constructor(long type);

    @SwiftConstructor
    @Owned
    @StaticSwiftMethod(symbol = "$s9swiftTest11DummyObjectCACycfC")
    public static native DummyObject constructorReturnsObjec(long type);


    @StaticSwiftMethod(symbol = "$s9swiftTest13getIsReleasedSbyF")
    public static native boolean isReleased();

}
