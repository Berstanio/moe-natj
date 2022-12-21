package org.moe.natj.swift.map;

import org.moe.natj.c.CRuntime;
import org.moe.natj.general.Mapper;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.ByValue;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.SwiftString;
import org.moe.natj.swift.ann.StaticSwiftMethod;

@Runtime(SwiftRuntime.class)
public class SwiftStringMapper implements Mapper {

    static {
        NatJ.register();
    }
    @Override
    public long toNative(Object instance, NatJ.NativeObjectConstructionInfo info) {
        long cPeer = CRuntime.createNativeString((String) instance);
        long swiftPeer = createSwiftString(cPeer).getPeerPointer();
        return swiftPeer;
    }

    @Override
    public Object toJava(long instance, NatJ.JavaObjectConstructionInfo info) {
        // TODO: 18.12.22 WHAT THE FUCK? Whyy??
        CRuntime.createJavaString(createCString(new SwiftString(new Pointer(instance))));
        return CRuntime.createJavaString(createCString(new SwiftString(new Pointer(instance))));
    }

    @StaticSwiftMethod(symbol = "$sSS7cStringSSSPys5UInt8VG_tcfC")
    @ByValue
    public static native SwiftString createSwiftString(long string);

    @StaticSwiftMethod(symbol = "$s4natj13createCStringySpys4Int8VGSSF")
    public static native long createCString(@ByValue SwiftString struct);
}
