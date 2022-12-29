package org.moe.swift.test.bindings;

import org.moe.natj.general.NatJ;
import org.moe.natj.general.ann.ByValue;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.StaticSwiftMethod;
import org.moe.natj.swift.ann.SwiftClosure;
import org.moe.swift.test.bindings.protocolTests.TestProtocol;

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

    @StaticSwiftMethod(symbol = "$s9swiftTest06stringB0yS2SF")
    public static native String stringTest(String s);

    @StaticSwiftMethod(symbol = "$s9swiftTest024getUnknownSubClassAsBaseF0AA0hF0CyF")
    public static native BaseClass getUnknownSubClassAsBaseClass();

    @StaticSwiftMethod(symbol = "$s9swiftTest25getUnknownClassAsProtocolAA0bG0_pyF")
    @ByValue
    public static native TestProtocol getUnknownProtocolClass();

    @StaticSwiftMethod(symbol = "$s9swiftTest23passBackUnknownProtocolySiAA0bF0_pF")
    public static native long passBackUnknownProtocol(@ByValue TestProtocol protocol);

    @StaticSwiftMethod(symbol = "$s9swiftTest011closurePassB0_10completionS2i_S2iXEtF")
    public static native long closurePassTest(long par1, @SwiftClosure Callback callback);

    @StaticSwiftMethod(symbol = "$s9swiftTest013closureReturnB0_10completionS2icSi_S2ictF")
    @SwiftClosure
    public static native Callback closureReturnTest(long par1, @SwiftClosure Callback callback);


    @Runtime(SwiftRuntime.class)
    public interface Callback {
        long callback_function(long par);
    }
}
