package org.moe.natj.swift;

import org.moe.natj.c.ann.Structure;
import org.moe.natj.c.ann.Variadic;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.NativeRuntime;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.ann.*;
import org.moe.natj.swift.map.SwiftObjectMapper;
import org.moe.natj.swift.map.SwiftStringMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

@Runtime(SwiftRuntime.class)
public class SwiftRuntime extends NativeRuntime {

    private static HashMap<Long, Class<?>> typeClassMap = new HashMap<>();
    private static HashMap<Class<?>, Long> classTypeMap = new HashMap<>();

    private static HashMap<Class<?>, Long> classProtocolDescriptorMap = new HashMap<>();
    private static HashMap<Long, HashMap<Class<?>, Long>> typeToClassWitnessTableMapMap = new HashMap<>();

    private static HashMap<Class<? extends SwiftEnumObject>, HashMap<Byte, Class<? extends SwiftEnumObject>>> enumToOrdinalCaseMapMap = new HashMap<>();

    static {
        NatJ.registerRuntime(SwiftRuntime.class);
        NatJ.register();
    }

    private SwiftRuntime() {
        super(SwiftObjectMapper.class, SwiftStringMapper.class, null);
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
        if (!type.isAnnotationPresent(SwiftBindingClass.class) && !type.isAnnotationPresent(Structure.class) && !type.isAnnotationPresent(SwiftEnum.class)) return;
        if (type.isAnnotationPresent(SwiftEnumCase.class)) return;
        // We can't wait for the cases to register, because maybe they wont because they are never referenced
        if (type.isAnnotationPresent(SwiftEnum.class)) {
            for (Class<?> c : type.getDeclaredClasses()) {
                if (!c.isAnnotationPresent(SwiftEnumCase.class)) continue;
                try {
                    byte ordinal = c.getDeclaredAnnotation(SwiftEnumCase.class).ordinal();
                    SwiftRuntime.registerEnumCase((Class<? extends SwiftEnumObject>) c, ordinal);
                    Field field = c.getDeclaredField("__ordinal");
                    field.setAccessible(true);
                    field.set(null, ordinal);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    System.err.println("Class " + c.getName() + " has no __ordinal field. The SwiftCase won't work.");
                }
            }
        }
        try {
            // TODO: 07.12.22 Solve better. Maybe a SwiftClass annotation? Or a SwiftMetadataType anno?
            Method method = type.getDeclaredMethod("getType");
            method.setAccessible(true);
            Long peer = (Long) method.invoke(null);
            registerMetadataPointer(type, peer);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("No getType for " + type.getName() + ", but is is either SwiftStruct or a SwiftBindingClass");
        }
    }

    public static void registerMetadataPointer(Class<?> type, long peer) {
        typeClassMap.put(peer, type);
        classTypeMap.put(type, peer);
    }

    public static long getClosestMetadataPointerFromInheritedClass(Class<?> type) {
        assert type != null;
        Class<?> found = type;
        while (!classTypeMap.containsKey(found)) {
            if (found == null) {
                System.out.println("Couldn't find matching metadata for " + type.getName());
                return 0;
            }
            found = found.getSuperclass();
        }
        return classTypeMap.get(found);
    }

    public static long getMetadataForClass(Class<?> aClass) {
        return classTypeMap.get(aClass);
    }

    public static Class<?> getClassForMetadataPointer(long peer) {
        Class<?> foundClass = typeClassMap.get(peer);
        if (foundClass == null) {
            System.err.printf("No class found for 0x%08X%n", peer);
        }
        return foundClass;
    }

    public static Class<?> getClassForPeer(long peer) {
        Class<?> foundClass = null;
        while (foundClass == null) {
            long metadataPeer = dereferencePeer(peer);
            if (metadataPeer == 0) break;
            foundClass = getClassForMetadataPointer(metadataPeer);
            peer = metadataPeer + 8;
        }
        return foundClass;
    }

    public static Class<?>[] getAllInheritedInterfaces(Class<?> type) {
        ArrayList<Class<?>> interfaces = new ArrayList<>();
        type = type.getSuperclass();
        while (type != null) {
            interfaces.addAll(Arrays.asList(type.getInterfaces()));
            type = type.getSuperclass();
        }
        return interfaces.stream().filter(aClass -> aClass.isAnnotationPresent(SwiftProtocol.class)).toArray(Class[]::new);
    }

    public static Method[] findOriginMethods(Method method) {
        ArrayList<Class<?>> interfaces = new ArrayList<>();
        Class<?> type = method.getDeclaringClass();
        while (type != null) {
            interfaces.addAll(Arrays.asList(type.getInterfaces()));
            type = type.getSuperclass();
        }
        return interfaces.stream()
                .filter(aClass -> aClass.isAnnotationPresent(SwiftProtocol.class))
                .map(aClass -> {
                    try {
                        return aClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
                    } catch (NoSuchMethodException e) {
                        return null;
                    }
                }).filter(Objects::nonNull).toArray(Method[]::new);
    }

    public static void registerProtocolClass(Class<?> type, long protocolDescriptor) {
        classProtocolDescriptorMap.put(type, protocolDescriptor);
    }

    public static void registerProtocolWitnessTable(long metadata, Class<?> type, long pwt) {
        HashMap<Class<?>, Long> classToWitnessTable = typeToClassWitnessTableMapMap.computeIfAbsent(metadata, k -> new HashMap<>());
        classToWitnessTable.put(type, pwt);
    }

    public static long getProtocolWitnessTable(long metadata, Class<?> type) {
        HashMap<Class<?>, Long> classToWitnessTable = typeToClassWitnessTableMapMap.computeIfAbsent(metadata, k -> new HashMap<>());
        return classToWitnessTable.computeIfAbsent(type, k -> fetchWitnessTable(metadata, classProtocolDescriptorMap.get(k)));
    }

    public static void registerEnumCase(Class<? extends SwiftEnumObject> swiftCase, byte ordinal){
        enumToOrdinalCaseMapMap
                .computeIfAbsent((Class<? extends SwiftEnumObject>) swiftCase.getEnclosingClass(), aClass -> new HashMap<>())
                .put(ordinal, swiftCase);
    }

    public static Class<? extends SwiftEnumObject> getEnumCase(Class<? extends SwiftEnumObject> swiftEnum, byte ordinal){
        return enumToOrdinalCaseMapMap.get(swiftEnum).get(ordinal);
    }

    // TODO: 07.12.22 Weeeell, this is silly, since it isn't a swift method... But since the conventions are so similar, it works
    @StaticSwiftMethod(symbol = "dereferencePeer")
    public static native long dereferencePeer(long peer);

    @StaticSwiftMethod(symbol = "setAtOffset")
    public static native long setAtOffset(long peer, long offset, byte toSet);

    @StaticSwiftMethod(symbol = "getAtOffset")
    public static native byte getAtOffset(long peer, long offset);

    @StaticSwiftMethod(symbol = "swift_retain")
    public static native void retain(long peer);

    @StaticSwiftMethod(symbol = "swift_release")
    public static native void release(long peer);

    @Deprecated
    @StaticSwiftMethod(symbol = "$ss12_autoreleaseyyyXlF")
    public static native void autorelease(long peer);

    @StaticSwiftMethod(symbol = "swift_conformsToProtocol")
    public static native long fetchWitnessTable(long metadata, long descriptor);

    public static native boolean forwardBooleanProtocolCall(Class<?> protocolClass, Method method, Object[] args);
    public static native byte forwardByteProtocolCall(Class<?> protocolClass, Method method, Object[] args);
    public static native char forwardCharProtocolCall(Class<?> protocolClass, Method method, Object[] args);
    public static native short forwardShortProtocolCall(Class<?> protocolClass, Method method, Object[] args);
    public static native int forwardIntProtocolCall(Class<?> protocolClass, Method method, Object[] args);
    public static native long forwardLongProtocolCall(Class<?> protocolClass, Method method, Object[] args);
    public static native float forwardFloatProtocolCall(Class<?> protocolClass, Method method, Object[] args);
    public static native double forwardDoubleProtocolCall(Class<?> protocolClass, Method method, Object[] args);
    public static native Object forwardObjectProtocolCall(Class<?> protocolClass, Method method, Object[] args);
    public static native void forwardVoidProtocolCall(Class<?> protocolClass, Method method, Object[] args);
}
