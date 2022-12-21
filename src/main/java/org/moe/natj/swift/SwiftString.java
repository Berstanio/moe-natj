package org.moe.natj.swift;

import org.moe.natj.c.StructObject;
import org.moe.natj.c.ann.Structure;
import org.moe.natj.c.ann.StructureField;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.Runtime;

@Structure()
@Runtime(SwiftRuntime.class)
public class SwiftString extends StructObject {

    static {
        NatJ.register();
    }

    private static long __natjCache;

    public SwiftString(Pointer peer) {
        super(peer);
    }

    @StructureField(order = 0, isGetter = true)
    public native long getField();
    @StructureField(order = 1, isGetter = true)
    public native long getRandomField();
}