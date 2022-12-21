package org.moe.swift.test.bindings;

import org.moe.natj.general.NatJ;
import org.moe.natj.general.ann.ByValue;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.StaticSwiftMethod;

@Runtime(SwiftRuntime.class)
public class SwiftStructTmp {

    static {
        NatJ.register();
    }

    @StaticSwiftMethod(symbol = "$s9swiftTest8testFuncySiAA0B6StructV_SitF")
    public static native long structTest(@ByValue TestStruct struct, long par2);

    @ByValue
    @StaticSwiftMethod(symbol = "$s9swiftTest0B6StructVACycfC")
    public static native TestStruct constructor();

    @StaticSwiftMethod(symbol = "$s9swiftTest0B6StructV03getC6NumberyS2iF")
    public static native long getStructNumber(long par1, @ByValue TestStruct struct);
}
