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
public class TestMediumStruct extends StructObject {

    static {
        NatJ.register();
    }

    private static long __natjCache;

    protected TestMediumStruct(Pointer peer) {
        super(peer);
    }

    public TestMediumStruct() {
        super(constructor().getPeer());
    }

    @StructureField(order = 0, isGetter = true)
    public native long getField1();

    @StructureField(order = 0, isGetter = false)
    public native void setField1(long field);

    @StructureField(order = 1, isGetter = true)
    public native long getField2();

    @StructureField(order = 1, isGetter = false)
    public native void setField2(long field);

    @StructureField(order = 2, isGetter = true)
    public native long getField3();

    @StructureField(order = 2, isGetter = false)
    public native void setField3(long field);

    @StaticSwiftMethod(symbol = "$s9swiftTest0B12MediumStructVMa")
    public static native long getType();

    @StaticSwiftMethod(symbol = "$s9swiftTest14testFuncMediumySiAA0B9BigStructV_SitF")
    public static native long structTest(@ByValue TestMediumStruct struct, long par2);

    @ByValue
    @StaticSwiftMethod(symbol = "$s9swiftTest0B12MediumStructVACycfC")
    public static native TestMediumStruct constructor();

    @StaticSwiftMethod(symbol = "$s9swiftTest0B12MediumStructV03getD6NumberyS2iF")
    public native long getStructNumber(long par1);

    @StaticSwiftMethod(symbol = "$s9swiftTest0B12MediumStructV03getD7Number2SiyF")
    public native long getStructNumber();

}
