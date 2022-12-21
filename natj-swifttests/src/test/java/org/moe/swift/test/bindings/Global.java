package org.moe.swift.test.bindings;

import org.moe.natj.general.NatJ;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.StaticSwiftMethod;

@Runtime(SwiftRuntime.class)
public class Global {

    static {
        NatJ.register();
    }

    @StaticSwiftMethod(symbol = "$s9swiftTest6intSum4par14par2S2i_SitF")
    public static native int intSum(int a, int b);

    @StaticSwiftMethod(symbol = "$s9swiftTest9doubleSum4par14par2S2d_SdtF")
    public static native double doubleSum(double a, double b);

    @StaticSwiftMethod(symbol = "$s9swiftTest8floatSum4par14par2S2f_SftF")
    public static native float floatSum(float a, float b);

    @StaticSwiftMethod(symbol = "$s9swiftTest04charB0yS2JF")
    public static native char charTest(char c);

    @StaticSwiftMethod(symbol = "$s9swiftTest04boolB0yS2bF")
    public static native boolean boolTest(boolean b);

}
