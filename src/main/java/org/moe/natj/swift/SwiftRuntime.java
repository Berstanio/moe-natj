package org.moe.natj.swift;

import org.moe.natj.c.ann.Variadic;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.NativeRuntime;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.ann.StaticSwiftMethod;
import org.moe.natj.swift.map.SwiftObjectMapper;

@Runtime(SwiftRuntime.class)
public class SwiftRuntime extends NativeRuntime {

    static {
        NatJ.registerRuntime(SwiftRuntime.class);
        NatJ.register();
    }

    private SwiftRuntime() {
        super(SwiftObjectMapper.class, null, null);
        initialize(this);
    }

    @Override
    public byte getDefaultUnboxPolicy() {
        return Variadic.Box;
    }

    @Override
    public void tryToDisposeCallback(Object callback) {

    }

    private static native void registerClass(Class<?> type);

    private static native void initialize(SwiftRuntime instance);

    @Override
    protected void doRegistration(Class<?> type) {
        registerClass(type);
    }

    @StaticSwiftMethod(symbol = "swift_retain")
    public static native void retain(long peer);

    @StaticSwiftMethod(symbol = "swift_release")
    public static native void release(long peer);

}
