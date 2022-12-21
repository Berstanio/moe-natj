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
public class TestBigStruct extends StructObject {

    static {
        NatJ.register();
    }

    private static long __natjCache;

    protected TestBigStruct(Pointer peer) {
        super(peer);
    }

    public TestBigStruct() {
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

    @StructureField(order = 3, isGetter = true)
    public native long getField4();

    @StructureField(order = 3, isGetter = false)
    public native void setField4(long field);

    @StructureField(order = 4, isGetter = true)
    public native long getField5();

    @StructureField(order = 4, isGetter = false)
    public native void setField5(long field);


    @StaticSwiftMethod(symbol = "$s9swiftTest0B9BigStructVMa")
    public static native long getType();

    @StaticSwiftMethod(symbol = "$s9swiftTest11testFuncBigySiAA0bE6StructV_SitF")
    public static native long structTest(@ByValue TestBigStruct struct, long par2);

    @ByValue
    @StaticSwiftMethod(symbol = "$s9swiftTest0B9BigStructVACycfC")
    public static native TestBigStruct constructor();

    @StaticSwiftMethod(symbol = "$s9swiftTest0B9BigStructV03getD6NumberyS2iF")
    public native long getStructNumber(long par1);

    @StaticSwiftMethod(symbol = "$s9swiftTest0B9BigStructV03getD7Number2SiyF")
    public native long getStructNumber();

}
