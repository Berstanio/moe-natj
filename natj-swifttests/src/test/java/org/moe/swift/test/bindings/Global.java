package org.moe.swift.test.bindings;

import org.moe.natj.general.NatJ;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.SwiftMethod;

@Runtime(SwiftRuntime.class)
public class Global {

    static {
        NatJ.register();
    }

    @SwiftMethod(symbol = "$s9swiftTest3sum4par14par2S2i_SitF")
    public static native int sum(int a, int b);
}
