package org.moe.swift.test.bindings.protocolTests;

import org.moe.natj.c.StructObject;
import org.moe.natj.c.ann.Structure;
import org.moe.natj.c.ann.StructureField;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.ByValue;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.StaticSwiftMethod;

@Structure()
@Runtime(SwiftRuntime.class)
public class SmallStructConformingProtocol extends StructObject implements TestProtocol {

    static {
        NatJ.register();
    }

    private static long __natjCache;

    protected SmallStructConformingProtocol(Pointer peer) {
        super(peer);
    }

    @StructureField(order = 0, isGetter = true)
    public native long getField();

    @StructureField(order = 0, isGetter = false)
    public native void setField(long field);

    @StructureField(order = 1, isGetter = true)
    public native long getField2();

    @StructureField(order = 1, isGetter = false)
    public native void setField2(long field);

    @StaticSwiftMethod(symbol = "$s9swiftTest29SmallStructConformingProtocolVMa")
    public static native long getType();
    @Override
    @StaticSwiftMethod(symbol = "$s9swiftTest29SmallStructConformingProtocolV9protoFuncSiyF")
    public native long protoFunc();

    @StaticSwiftMethod(symbol = "$s9swiftTest24getSmallStructAsProtocolAA0bG0_pyF")
    @ByValue
    public static native TestProtocol getSmallStructAsProtocol();

}
