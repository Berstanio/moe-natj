package org.moe.swift.test.bindings;

import org.moe.natj.general.NatJ;
import org.moe.natj.general.NativeObject;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.SwiftConstructor;
import org.moe.natj.swift.ann.StaticSwiftMethod;
import org.moe.natj.swift.ann.VirtualSwiftMethod;

@Runtime(SwiftRuntime.class)
public class BaseClass extends NativeObject {

    static {
        NatJ.register();
    }

    /**
     * Constructs a {@link NativeObject} from a {@link Pointer}.
     *
     * @param peer The pointer pointing to the native peer.
     */
    public BaseClass(Pointer peer) {
        super(peer);
    }

    public BaseClass() {
        super(new Pointer(constructor(getType())));
    }

    @StaticSwiftMethod(symbol = "$s9swiftTest9BaseClassCMa")
    private static native long getType();

    @SwiftConstructor
    @StaticSwiftMethod(symbol = "$s9swiftTest9BaseClassCACycfC")
    private static native long constructor(long type);

    @VirtualSwiftMethod(offset = 88)
    public native int getClassSpecificNumber();

    @VirtualSwiftMethod(offset = 96)
    public native int getBaseClassField();

}
