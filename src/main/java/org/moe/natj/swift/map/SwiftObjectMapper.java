package org.moe.natj.swift.map;

import org.moe.natj.c.CRuntime;
import org.moe.natj.c.StructObject;
import org.moe.natj.general.Mapper;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.NativeObject;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ptr.LongPtr;
import org.moe.natj.general.ptr.impl.PtrFactory;
import org.moe.natj.swift.ProtocolProxyHandler;
import org.moe.natj.swift.SwiftRuntime;
import org.moe.natj.swift.ann.SwiftBindingClass;
import org.moe.natj.swift.ann.SwiftProtocol;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;

public class SwiftObjectMapper implements Mapper {

    private HashMap<Long, Object> inheritedObjectMap = new HashMap<>();

    private long wrapObjectInEC(Object instance, Class<?> type) {
        long peer = ((NativeObject) instance).getPeer().getPeer();
        long metadata;
        boolean copyStruct = false;
        long size = 0;
        if (isStruct(instance.getClass())) {
            metadata = SwiftRuntime.getMetadataForClass(instance.getClass());
            size = CRuntime.sizeOfNativeObject((Class<? extends NativeObject>) instance.getClass());
            if (size <= 24) {
                copyStruct = true;
            } else {
                long newPeer = CRuntime.malloc(size + 16);
                // TODO: 11.12.22 Shouldn't it be a long as size?
                CRuntime.memcpy(newPeer + 16, peer, (int)size);
                // TODO: 11.12.22 We need to fill the 16 bytes with useful information
                peer = newPeer;
            }
        } else {
            metadata = SwiftRuntime.dereferencePeer(peer);
        }

        long protocolWitnessTable = SwiftRuntime.getProtocolWitnessTable(metadata, type);
        long[] ec = new long[] {peer, 0, 0, metadata, protocolWitnessTable};
        LongPtr ptr = PtrFactory.newLongArray(ec);

        if (copyStruct) {
            CRuntime.memcpy(ptr.getPeer().getPeer(), peer, (int)size);
        }

        // TODO: 10.12.22 Also, who is supposed to handle freeing? IDK!
        return ptr.getPeer().getPeer();
    }

    @Override
    public long toNative(Object instance, NatJ.NativeObjectConstructionInfo info) {
        if (instance == null) {
            return 0;
        }

        if (Proxy.isProxyClass(instance.getClass())) {
            ProtocolProxyHandler.ProtocolProxy protocolProxy = (ProtocolProxyHandler.ProtocolProxy) Proxy.getInvocationHandler(instance);
            return protocolProxy.getPeer();
        }

        if (info.packWithEC) {
            return wrapObjectInEC(instance, (Class<?>) info.data);
        }

        boolean isInherited = !isStruct(instance.getClass()) && !instance.getClass().isAnnotationPresent(SwiftBindingClass.class);

        // TODO: 20.12.22 DEFINITLY SOLVE BETTER!!!
        if (isInherited) {
            inheritedObjectMap.putIfAbsent(((NativeObject) instance).getPeer().getPeer(), instance);
        }

        // TODO: 13.12.22 For now it seems like, we can pass structs just by reference. Since swift functions can't just alter it,
        //  at least code doing that wouldn't compile. How the "mutating" keyword works will be needed to investigated later
        return ((NativeObject) instance).getPeer().getPeer();
    }

    private boolean isStruct(Class<?> aClass) {
        return StructObject.class.isAssignableFrom(aClass);
    }

    public Object constructJavaObjectWithConstructor(long instance, Class<?> toInstantiate, NatJ.JavaObjectConstructionInfo info) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor;

        // TODO: 07.12.22 Only with getConstructor? To improve inheritance?
        // TODO: 20.12.22 Add caching?
        constructor = toInstantiate.getDeclaredConstructor(Pointer.class);
        constructor.setAccessible(true);

        boolean isInherited = !isStruct(toInstantiate) && !toInstantiate.isAnnotationPresent(SwiftBindingClass.class);
        Object javaObject;
        if (isInherited) {
            javaObject = inheritedObjectMap.computeIfAbsent(instance, (peer) -> {
                try {
                    return constructor.newInstance(new Pointer(peer));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
             javaObject = constructor.newInstance(new Pointer(instance));
        }

        return javaObject;
    }

    public Object constructJavaObjectForProxy(long instance, NatJ.JavaObjectConstructionInfo info) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Look at definition of existential container, to see where the 24 comes from
        // 3 * value, 1 * metadata, 1 * pwt
        long metadataPointer = SwiftRuntime.dereferencePeer(instance + 24);
        Class<?> classForInstance = SwiftRuntime.getClassForMetadataPointer(metadataPointer);
        if (classForInstance == null) return ProtocolProxyHandler.createProtocolProxy(instance, info.type);

        // TODO: 08.12.22 For structs the EC doesn't directly point to the struct. So we need a small offset
        if (isStruct(classForInstance)) {
            long size = CRuntime.sizeOfNativeObject((Class<? extends NativeObject>) classForInstance);
            if (size > 24) {
                instance = SwiftRuntime.dereferencePeer(instance);
                instance += 16;
            } else {
                long struct = CRuntime.malloc(size);
                CRuntime.memcpy(struct, instance, (int) size);
                instance = struct;
            }
        } else {
            instance = SwiftRuntime.dereferencePeer(instance);
        }

        return constructJavaObjectWithConstructor(instance, classForInstance, info);
    }

    @Override
    public Object toJava(long instance, NatJ.JavaObjectConstructionInfo info) {
        try {
            synchronized (info) {
                boolean isProtocolReturn = info.type.isAnnotationPresent(SwiftProtocol.class) || info.unpackAsEC;
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
