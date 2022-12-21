package org.moe.natj.swift.map;

import org.moe.natj.c.StructObject;
import org.moe.natj.general.Mapper;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.NativeObject;
import org.moe.natj.general.Pointer;
import org.moe.natj.swift.ProtocolProxyHandler;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.SwiftProtocol;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class SwiftObjectMapper implements Mapper {
    @Override
    public long toNative(Object instance, NatJ.NativeObjectConstructionInfo info) {
        if (instance == null) {
            return 0;
        }

        if (Proxy.isProxyClass(instance.getClass())) {
            ProtocolProxyHandler.ProtocolProxy protocolProxy = (ProtocolProxyHandler.ProtocolProxy) Proxy.getInvocationHandler(instance);
            return protocolProxy.getPeer();
        }

        // TODO: 09.12.22 Pack in EC if protocol parameter

        return ((NativeObject) instance).getPeer().getPeer();
    }

    private boolean isStruct(Class<?> aClass) {
        return StructObject.class.isAssignableFrom(aClass);
    }

    public Object constructJavaObjectWithConstructor(long instance, Class<?> toInstantiate, NatJ.JavaObjectConstructionInfo info) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor;
        if (toInstantiate != null) {
            // TODO: 07.12.22 Only with getConstructor? To improve inheritance?
            constructor = toInstantiate.getDeclaredConstructor(Pointer.class);
            constructor.setAccessible(true);
        } else if (info.data == null) {
            constructor = info.type.getDeclaredConstructor(Pointer.class);
            constructor.setAccessible(true);
            info.data = constructor;
        } else {
            constructor = ((Constructor<?>) info.data);
        }
        return constructor.newInstance(new Pointer(instance));
    }

    public Object constructJavaObjectForProxy(long instance, NatJ.JavaObjectConstructionInfo info) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Look at definition of existential container, to see where the 24 comes from
        // 3 * value, 1 * metadata, 1 * pwt
        long metadataPointer = SwiftRuntime.dereferencePeer(instance + 24);
        Class<?> classForInstance = SwiftRuntime.getClassForMetadataPointer(metadataPointer);
        if (classForInstance == null) return ProtocolProxyHandler.createProtocolProxy(instance, info.type);
        instance = SwiftRuntime.dereferencePeer(instance);
        // TODO: 08.12.22 For structs the EC doesn't directly point to the struct. So we need a small offset
        if (isStruct(classForInstance)) instance += 16;

        return constructJavaObjectWithConstructor(instance, classForInstance, info);
    }

    @Override
    public Object toJava(long instance, NatJ.JavaObjectConstructionInfo info) {
        try {
            synchronized (info) {
                boolean isProtocolReturn = info.type.isAnnotationPresent(SwiftProtocol.class);
                if (isProtocolReturn) {
                    return constructJavaObjectForProxy(instance, info);
                } else {
                    Class<?> toInstance = info.type;
                    if (!isStruct(toInstance)) {
                        toInstance = SwiftRuntime.getClassForPeer(instance);
                    }
                    return constructJavaObjectWithConstructor(instance, toInstance, info);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Java object construction error!", ex);
        }
    }
}
