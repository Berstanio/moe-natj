package org.moe.swift.test.bindings.protocolTests;

import org.moe.natj.general.NatJ;
import org.moe.natj.general.NativeObject;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.ByValue;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.StaticSwiftMethod;
import org.moe.natj.swift.ann.SwiftBindingClass;
import org.moe.natj.swift.ann.SwiftConstructor;
import org.moe.natj.swift.ann.VirtualSwiftMethod;

@Runtime(SwiftRuntime.class)
@SwiftBindingClass
public class ClassConformingProtocol extends NativeObject implements TestProtocol {

    static {
        NatJ.register();
    }


    /**
     * Constructs a {@link NativeObject} from a {@link Pointer}.
     *
     * @param peer The pointer pointing to the native peer.
     */
    protected ClassConformingProtocol(Pointer peer) {
        super(peer);
    }

    public ClassConformingProtocol() {
        super(new Pointer(constructor(getType())));
    }

    @StaticSwiftMethod(symbol = "$s9swiftTest23ClassConformingProtocolCMa")
    public static native long getType();

    @SwiftConstructor
    @StaticSwiftMethod(symbol = "$s9swiftTest23ClassConformingProtocolCACycfC")
    private static native long constructor(long type);


    @Override
    @VirtualSwiftMethod(offset = 80)
    public native long protoFunc();

    @StaticSwiftMethod(symbol = "$s9swiftTest18getClassAsProtocolAA0bF0_pyF")
    @ByValue
    public static native TestProtocol getClassAsProtocol();
}
