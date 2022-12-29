package org.moe.natj.swift.map;

import org.moe.natj.general.Mapper;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.ann.Runtime;
import org.moe.natj.swift.SwiftRuntime;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.WeakHashMap;

@Runtime(SwiftRuntime.class)
public class SwiftClosureMapper implements Mapper {

    private WeakHashMap<Object, Long> instanceClosureMap = new WeakHashMap<>();
    private HashMap<Long, WeakReference<Object>> closureInstanceMap = new HashMap<>();

    @Override
    public long toNative(Object instance, NatJ.NativeObjectConstructionInfo info) {
        if (instanceClosureMap.containsKey(instance)) return instanceClosureMap.get(instance);
        long peer = SwiftRuntime.createSwiftClosure(instance, (Class<?>) info.data);
        instanceClosureMap.put(instance, peer);
        closureInstanceMap.put(peer, new WeakReference<>(instance));
        return peer;
    }

    @Override
    public Object toJava(long instance, NatJ.JavaObjectConstructionInfo info) {
        if (closureInstanceMap.containsKey(instance)) return closureInstanceMap.get(instance);
        throw new RuntimeException("Implement unknown closures");
    }
}
