package org.moe.swift.test.bindings;

import org.moe.natj.c.StructObject;
import org.moe.natj.c.ann.Structure;
import org.moe.natj.c.ann.StructureField;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.ByValue;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.StaticSwiftMethod;
import org.moe.natj.swift.ann.StructMutating;

@Structure()
@Runtime(SwiftRuntime.class)
public class SubscriptStruct extends StructObject {

    static {
        NatJ.register();
    }

    protected SubscriptStruct(Pointer peer) {
        super(peer);
    }

    public SubscriptStruct(){
        super(constructor(getType()).getPeer());
    }

    private static long __natjCache;

    @StaticSwiftMethod(symbol = "$s9swiftTest15SubscriptStructVMa")
    public static native long getType();

    @StaticSwiftMethod(symbol = "$s9swiftTest15SubscriptStructVACycfC")
    @ByValue
    public static native SubscriptStruct constructor(long type);

    @StaticSwiftMethod(symbol = "$s9swiftTest15SubscriptStructVyS2icig")
    public native long get(long i);

    @StaticSwiftMethod(symbol = "$s9swiftTest15SubscriptStructVyS2icis")
    @StructMutating
    public native void set(long index, long newValue);

    @StructureField(order = 0, isGetter = true)
    public native long getSubscriptValue();

    @StructureField(order = 0, isGetter = false)
    public native void setSubscriptValue(long subscriptValue);


}
