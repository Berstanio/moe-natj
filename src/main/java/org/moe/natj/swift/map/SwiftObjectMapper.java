package org.moe.natj.swift.map;

import org.moe.natj.general.Mapper;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.NativeObject;
import org.moe.natj.general.Pointer;

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
                if (info.data == null) {
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
