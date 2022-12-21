package org.moe.swift.test.bindings;

import org.moe.natj.general.NatJ;
import org.moe.natj.general.NativeObject;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.SwiftBindingClass;
import org.moe.natj.swift.ann.SwiftConstructor;
import org.moe.natj.swift.ann.StaticSwiftMethod;
import org.moe.natj.swift.ann.VirtualSwiftMethod;

@Runtime(SwiftRuntime.class)
@SwiftBindingClass
public class SubClass extends BaseClass {

    static {
        NatJ.register();
    }

    /**
     * Constructs a {@link NativeObject} from a {@link Pointer}.
     *
     * @param peer The pointer pointing to the native peer.
     */
    protected SubClass(Pointer peer) {
        super(peer);
    }

    public SubClass() {
        super(new Pointer(constructor(getType())));
    }

    @StaticSwiftMethod(symbol = "$s9swiftTest8SubClassCMa")
    public static native long getType();

    @SwiftConstructor
    @StaticSwiftMethod(symbol = "$s9swiftTest8SubClassCACycfC")
    private static native long constructor(long type);

    @VirtualSwiftMethod(offset = 168)
    public native int onlySubClass();

    @StaticSwiftMethod(symbol = "$s9swiftTest8SubClassC03getcd6AsBaseD0AA0gD0CyFZ")
    public static native BaseClass getSubClassAsBaseClass();
}
