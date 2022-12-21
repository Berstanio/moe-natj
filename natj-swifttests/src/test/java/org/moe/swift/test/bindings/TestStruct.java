package org.moe.swift.test.bindings;

import org.moe.natj.c.StructObject;
import org.moe.natj.c.ann.Structure;
import org.moe.natj.c.ann.StructureField;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;

@Structure()
public class TestStruct extends StructObject {

    static {
        NatJ.register();
    }

    private static long __natjCache;

    protected TestStruct(Pointer peer) {
        super(peer);
    }

    public TestStruct() {
        super(TestStruct.class);
    }

    @StructureField(order = 0, isGetter = true)
    public native long getField();

    @StructureField(order = 0, isGetter = false)
    public native void setField(long field);

    @StructureField(order = 1, isGetter = true)
    public native long getRandomField();

    @StructureField(order = 1, isGetter = false)
    public native void setRandomField(long field);

}
