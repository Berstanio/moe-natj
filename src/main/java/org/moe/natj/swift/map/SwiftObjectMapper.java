package org.moe.natj.swift.map;

import org.moe.natj.c.StructObject;
import org.moe.natj.general.Mapper;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.NativeObject;
import org.moe.natj.general.Pointer;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.SwiftProtocol;

import java.lang.reflect.Constructor;

public class SwiftObjectMapper implements Mapper {
    @Override
    public long toNative(Object instance, NatJ.NativeObjectConstructionInfo info) {
        if (instance == null) {
            return 0;
        }
        return ((NativeObject) instance).getPeer().getPeer();
    }

    @Override
    public Object toJava(long instance, NatJ.JavaObjectConstructionInfo info) {
        Pointer pointer = new Pointer(instance);
        try {
            Constructor<?> constructor;
            synchronized (info) {
                Class<?> classForInstance = null;
                boolean isProtocolReturn = info.type.isAnnotationPresent(SwiftProtocol.class);
                if (isProtocolReturn) {
                    classForInstance = SwiftRuntime.getClassForPeer(SwiftRuntime.dereferencePeer(instance));
                    pointer.setPeer(SwiftRuntime.dereferencePeer(instance));
                    if (classForInstance == null) throw new RuntimeException("No binding found for protocol, currently unsupported");
                } else if (!StructObject.class.isAssignableFrom(info.type)) {
                    classForInstance = SwiftRuntime.getClassForPeer(instance);
                }
                if (classForInstance != null) {
                    // TODO: 07.12.22 Only with getConstructor? To improve inheritance?
                    constructor = classForInstance.getDeclaredConstructor(Pointer.class);
                    constructor.setAccessible(true);
                } else if (info.data == null) {
                    constructor = info.type.getDeclaredConstructor(Pointer.class);
                    constructor.setAccessible(true);
                    info.data = constructor;
                } else {
                    constructor = ((Constructor<?>) info.data);
                }
            }
            return constructor.newInstance(pointer);
        } catch (Exception ex) {
            throw new RuntimeException("Java object construction error!", ex);
        }
    }
}
