package org.moe.natj.swift;

import org.moe.natj.c.StructObject;
import org.moe.natj.c.ann.Structure;
import org.moe.natj.c.ann.StructureField;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.ByValue;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.ann.IndirectReturnX8;
import org.moe.natj.swift.ann.StaticSwiftMethod;

@Structure
@Runtime(SwiftRuntime.class)
// TODO: 20.03.23 The layout is heavily based on guessing
public class SwiftArray<T> extends StructObject {

    static {
        NatJ.register();
    }

    private static long __natjCache;

    protected SwiftArray(Pointer peer) {
        super(peer);
    }

    @StructureField(order = 0, isGetter = true)
    public native long getPointer();

    @StaticSwiftMethod(symbol = "$ss6UInt64VMa")
    public static native long getType();

    @StaticSwiftMethod(symbol = "$sSayxSicig")
    @IndirectReturnX8
    public static native long getNumber(long index, @ByValue SwiftArray array, long type);


    @StaticSwiftMethod(symbol = "$sSa5countSivg")
    public native long count();


    // This is also heaavily based on guessing and not generic at all
    public long[] getAsJavaArray() {
        long count = count();
        long[] array = new long[(int) count];
        for (int i = 0; i < count; i++) {
            array[i] = get(i);
        }
        return array;
    }

    public long get(int idx) {
        return getNumber(idx, this, getType());
    }
}
