package org.moe.swift.test.bindings;

import org.moe.natj.general.NatJ;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.StaticSwiftMethod;
import org.moe.natj.swift.ann.VirtualSwiftMethod;

@Runtime(SwiftRuntime.class)
public class JavaSideSubClass extends BaseClass {

    static {
        NatJ.register();
    }

    public JavaSideSubClass(Pointer peer) {
        super(peer);
    }

    public JavaSideSubClass() {
        super();
    }


    @Override
    @VirtualSwiftMethod(offset = 88)
    public int getClassSpecificNumber() {
        return 42;
    }

    @StaticSwiftMethod(symbol = "$s9swiftTest28globalGetClassSpecificNumberySiAA04BaseE0CF")
    public static native long getClassNumberGlobal(BaseClass baseClass);
}
