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

    private boolean isStruct(Class<?> aClass) {
        return StructObject.class.isAssignableFrom(aClass);
    }

    private Class<?> classToInstantiate(Pointer pointer, Class<?> type) {
        long instance = pointer.getPeer();
        boolean isProtocolReturn = type.isAnnotationPresent(SwiftProtocol.class);
        Class<?> classForInstance = null;
        if (isProtocolReturn) {
            // Look at definition of existential container, to see where the 24 comes from
            // 3 * value, 1 * metadata, 1 * pwt
            long metadataPointer = SwiftRuntime.dereferencePeer(instance + 24);
            classForInstance = SwiftRuntime.getClassForMetadataPointer(metadataPointer);
            if (classForInstance == null) throw new RuntimeException("No binding found for protocol, currently unsupported");
            instance = SwiftRuntime.dereferencePeer(instance);
            // TODO: 08.12.22 For structs the EC doesn't directly point to the struct. So we need a small offset
            if (isStruct(classForInstance)) instance += 16;
            pointer.setPeer(instance);
        } else if (!isStruct(type)) {
            classForInstance = SwiftRuntime.getClassForPeer(instance);
        }
        pointer.setPeer(instance);
        return classForInstance;
    }

    @Override
    public Object toJava(long instance, NatJ.JavaObjectConstructionInfo info) {
        Pointer pointer = new Pointer(instance);
        try {
            Constructor<?> constructor;
            synchronized (info) {
                Class<?> classForInstance = classToInstantiate(pointer, info.type);
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
