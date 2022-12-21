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

@Structure()
@Runtime(SwiftRuntime.class)
public class TestStruct extends StructObject {

    static {
        NatJ.register();
    }

    private static long __natjCache;

    protected TestStruct(Pointer peer) {
        super(peer);
    }

    public TestStruct() {
        super(constructor().getPeer());
    }

    @StructureField(order = 0, isGetter = true)
    public native long getField();

    @StructureField(order = 0, isGetter = false)
    public native void setField(long field);

    @StructureField(order = 1, isGetter = true)
    public native long getRandomField();

    @StructureField(order = 1, isGetter = false)
    public native void setRandomField(long field);

    @StaticSwiftMethod(symbol = "$s9swiftTest0B6StructVMa")
    public static native long getType();

    @StaticSwiftMethod(symbol = "$s9swiftTest8testFuncySiAA0B6StructV_SitF")
    public static native long structTest(@ByValue TestStruct struct, long par2);

    @ByValue
    @StaticSwiftMethod(symbol = "$s9swiftTest0B6StructVACycfC")
    public static native TestStruct constructor();

    @StaticSwiftMethod(symbol = "$s9swiftTest0B6StructV03getC6NumberyS2iF")
    public static native long getStructNumber(long par1, @ByValue TestStruct struct);


}
